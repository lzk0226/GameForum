package com.ruoyi.user.service.impl;

import com.ruoyi.user.domain.Post;
import com.ruoyi.user.mapper.PostMapper;
import com.ruoyi.user.service.IPostRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 实时帖子推荐服务实现类
 */
@Service
public class PostRecommendationServiceImpl implements IPostRecommendationService {

    @Autowired
    private PostMapper postMapper;

    // 用户行为实时缓存 - 内存中存储最近的用户行为
    //private static final Map<Long, List<UserRecentBehavior>> userRecentBehaviors = new ConcurrentHashMap<>();
    private final Map<Long, List<UserRecentBehavior>> userRecentBehaviors = new ConcurrentHashMap<>();
    private static final long BEHAVIOR_EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时过期
    private static final int MAX_RECENT_BEHAVIORS = 100; // 每个用户最多保存100个最近行为

    /**
     * 用户最近行为数据结构
     */
    private static class UserRecentBehavior {
        Integer postId;
        Double score;
        String behaviorType;
        long timestamp;
        Integer sectionId; // 添加版块信息用于内容推荐

        public UserRecentBehavior(Integer postId, Double score, String behaviorType, Integer sectionId) {
            this.postId = postId;
            this.score = score;
            this.behaviorType = behaviorType;
            this.sectionId = sectionId;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > BEHAVIOR_EXPIRE_TIME;
        }
    }


    /**
     * 记录用户行为 - 修复版本，清除相关用户的缓存
     */
    public void recordUserBehavior(Long userId, Integer postId, String behaviorType, Integer sectionId) {
        if (userId == null) return;

        double score = getScoreByBehaviorType(behaviorType);
        UserRecentBehavior behavior = new UserRecentBehavior(postId, score, behaviorType, sectionId);

        userRecentBehaviors.computeIfAbsent(userId, k -> new ArrayList<>()).add(behavior);

        // 清理过期行为和限制数量
        cleanUpUserBehaviors(userId);

        // 清除相关用户的推荐缓存
        evictRelatedUserCaches(userId, postId);
    }

    /**
     * 清除相关用户的推荐缓存 - 新增方法
     */
    private void evictRelatedUserCaches(Long userId, Integer postId) {
        // 1. 清除当前用户的缓存
        evictUserRecommendationCache(userId);

        // 2. 找到可能受影响的相似用户并清除他们的缓存
        List<Long> similarUsers = findSimilarUsersByRecentBehavior(userId,
                getUserRecentBehaviors(userId));

        for (Long similarUserId : similarUsers) {
            evictUserRecommendationCache(similarUserId);
        }

    }

    /**
     * 根据行为类型获取评分
     */
    private double getScoreByBehaviorType(String behaviorType) {
        switch (behaviorType) {
            case "like": return 3.0;
            case "comment": return 2.5;
            case "post": return 1.5;
            case "view": return 0.5;
            case "share": return 2.0;
            default: return 1.0;
        }
    }

    /**
     * 清理用户过期行为
     */
    private void cleanUpUserBehaviors(Long userId) {
        List<UserRecentBehavior> behaviors = userRecentBehaviors.get(userId);
        if (behaviors == null) return;

        // 移除过期行为
        behaviors.removeIf(UserRecentBehavior::isExpired);

        // 限制数量，保留最新的行为
        if (behaviors.size() > MAX_RECENT_BEHAVIORS) {
            behaviors.sort((a, b) -> Long.compare(b.timestamp, a.timestamp));
            List<UserRecentBehavior> limitedBehaviors = new ArrayList<>(behaviors.subList(0, MAX_RECENT_BEHAVIORS));
            userRecentBehaviors.put(userId, limitedBehaviors);
        }
    }

    /**
     * 清除用户推荐缓存
     */
    @CacheEvict(value = "userRecommendations", key = "#userId")
    public void evictUserRecommendationCache(Long userId) {}

    /**
     * 获取个性化推荐帖子列表 - 修改缓存策略
     */
    @Override
    @Cacheable(value = "userRecommendations",
            key = "#userId + '_' + T(System).currentTimeMillis() / 300000", // 5分钟缓存
            unless = "#result.size() == 0")
    public List<Post> getRecommendedPosts(Long userId, Integer limit) {
        // 原有逻辑保持不变
        List<UserRecentBehavior> recentBehaviors = getUserRecentBehaviors(userId);

        if (recentBehaviors.isEmpty()) {
            return getRecommendationsWithHistoricalData(userId, limit);
        }

        List<Post> realtimeRecommendations = generateRealtimeRecommendations(userId, recentBehaviors, limit);

        if (realtimeRecommendations.size() < limit) {
            List<Post> historicalRecommendations = getRecommendationsWithHistoricalData(userId, limit - realtimeRecommendations.size());

            Set<Integer> existingPostIds = realtimeRecommendations.stream()
                    .map(Post::getPostId)
                    .collect(Collectors.toSet());

            for (Post post : historicalRecommendations) {
                if (existingPostIds.add(post.getPostId())) {
                    realtimeRecommendations.add(post);
                    if (realtimeRecommendations.size() >= limit) break;
                }
            }
        }

        return new ArrayList<>(realtimeRecommendations);
    }

    /**
     * 获取用户最近行为
     */
    private List<UserRecentBehavior> getUserRecentBehaviors(Long userId) {
        List<UserRecentBehavior> behaviors = userRecentBehaviors.get(userId);
        if (behaviors == null) return new ArrayList<>();

        // 清理过期行为
        cleanUpUserBehaviors(userId);
        return behaviors;
    }

    /**
     * 基于最近行为生成实时推荐
     */
    private List<Post> generateRealtimeRecommendations(Long userId, List<UserRecentBehavior> recentBehaviors, Integer limit) {
        // 1. 分析用户最近偏好的版块
        Map<Integer, Double> sectionPreferences = analyzeSectionPreferences(recentBehaviors);

        // 2. 分析相似用户的最近行为
        List<Long> similarUsers = findSimilarUsersByRecentBehavior(userId, recentBehaviors);

        // 3. 生成推荐评分
        Map<Integer, Double> postScores = new HashMap<>();

        // 3.1 基于版块偏好推荐
        for (Map.Entry<Integer, Double> entry : sectionPreferences.entrySet()) {
            Integer sectionId = entry.getKey();
            Double preference = entry.getValue();

            List<Post> sectionPosts = postMapper.selectRecentHotPostsBySectionId(sectionId, 10);
            for (Post post : sectionPosts) {
                if (!hasUserInteractedRecently(userId, post.getPostId())) {
                    postScores.merge(post.getPostId(), preference * 0.7, Double::sum);
                }
            }
        }

        // 3.2 基于相似用户的最近行为
        for (Long similarUserId : similarUsers) {
            List<UserRecentBehavior> similarUserBehaviors = getUserRecentBehaviors(similarUserId);
            for (UserRecentBehavior behavior : similarUserBehaviors) {
                if (!hasUserInteractedRecently(userId, behavior.postId)) {
                    double timeDecay = calculateTimeDecay(behavior.timestamp);
                    postScores.merge(behavior.postId, behavior.score * 0.5 * timeDecay, Double::sum);
                }
            }
        }

        // 4. 排序并返回推荐结果
        return postScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(limit != null ? limit : 10)
                .map(entry -> postMapper.selectPostById(entry.getKey()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 分析版块偏好
     */
    private Map<Integer, Double> analyzeSectionPreferences(List<UserRecentBehavior> behaviors) {
        Map<Integer, Double> preferences = new HashMap<>();

        for (UserRecentBehavior behavior : behaviors) {
            if (behavior.sectionId != null) {
                double timeDecay = calculateTimeDecay(behavior.timestamp);
                preferences.merge(behavior.sectionId, behavior.score * timeDecay, Double::sum);
            }
        }

        return preferences;
    }

    /**
     * 时间衰减计算
     */
    private double calculateTimeDecay(long timestamp) {
        long hoursPassed = (System.currentTimeMillis() - timestamp) / (1000 * 60 * 60);
        return Math.exp(-hoursPassed / 24.0); // 24小时衰减到1/e
    }

    /**
     * 基于最近行为找相似用户
     */
    private List<Long> findSimilarUsersByRecentBehavior(Long userId, List<UserRecentBehavior> userBehaviors) {
        Map<Long, Double> similarities = new HashMap<>();
        Set<Integer> userInteractedPosts = userBehaviors.stream()
                .map(b -> b.postId)
                .collect(Collectors.toSet());

        for (Map.Entry<Long, List<UserRecentBehavior>> entry : userRecentBehaviors.entrySet()) {
            Long otherUserId = entry.getKey();
            if (otherUserId.equals(userId)) continue;

            List<UserRecentBehavior> otherBehaviors = entry.getValue();
            Set<Integer> otherInteractedPosts = otherBehaviors.stream()
                    .map(b -> b.postId)
                    .collect(Collectors.toSet());

            // 计算交集相似度
            Set<Integer> intersection = new HashSet<>(userInteractedPosts);
            intersection.retainAll(otherInteractedPosts);

            if (!intersection.isEmpty()) {
                double similarity = (double) intersection.size() /
                        Math.sqrt(userInteractedPosts.size() * otherInteractedPosts.size());
                similarities.put(otherUserId, similarity);
            }
        }

        return similarities.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getRecommendedPostsWithPaging(Long userId, Integer limit, Integer offset) {
        // 获取更大的推荐池
        int totalNeeded = (limit != null ? limit : 10) + (offset != null ? offset : 0) + 20;
        List<Post> allRecommendations = getRecommendedPosts(userId, totalNeeded);

        if (allRecommendations == null || allRecommendations.isEmpty()) {
            return new ArrayList<>();
        }

        int start = offset != null ? offset : 0;
        int end = Math.min(start + (limit != null ? limit : 10), allRecommendations.size());

        if (start >= allRecommendations.size()) {
            return new ArrayList<>();
        }

        return allRecommendations.subList(start, end);
    }

    /**
     * 混合推荐分页方法 - 支持分页
     */
    public List<Post> getHybridRecommendationsWithPaging(Long userId, Integer limit, Integer page) {
        if (userId == null || limit == null || page == null || page < 1) {
            return new ArrayList<>();
        }

        // 每页推荐数量
        int pageSize = limit;
        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 生成足够大的推荐池来支持分页
        // 为了保证分页的连续性，我们需要生成更多的推荐
        int poolSize = Math.max(offset + pageSize * 3, 100); // 至少生成当前页面需要的数量的3倍

        List<Post> allRecommendations = getHybridRecommendations(userId, poolSize);

        if (allRecommendations == null || allRecommendations.isEmpty()) {
            // 如果推荐系统失败，返回热门帖子
            List<Post> hotPosts = postMapper.selectHotPostList(pageSize);
            return hotPosts != null ? hotPosts : new ArrayList<>();
        }

        // 执行分页切割
        if (offset >= allRecommendations.size()) {
            return new ArrayList<>(); // 超出范围，返回空列表
        }

        int endIndex = Math.min(offset + pageSize, allRecommendations.size());
        return allRecommendations.subList(offset, endIndex);
    }

    /**
     * 检查用户是否最近与帖子交互过
     */
    private boolean hasUserInteractedRecently(Long userId, Integer postId) {
        List<UserRecentBehavior> behaviors = userRecentBehaviors.get(userId);
        if (behaviors == null) return false;

        return behaviors.stream().anyMatch(b -> b.postId.equals(postId));
    }

    /**
     * 使用历史数据的推荐（原有逻辑）
     */
    private List<Post> getRecommendationsWithHistoricalData(Long userId, Integer limit) {
        // 收集历史行为数据
        List<UserBehavior> allBehaviors = collectUserBehaviors();
        Map<Long, Map<Integer, Double>> userPostMatrix = buildUserPostMatrix(allBehaviors);

        if (!userPostMatrix.containsKey(userId)) {
            return postMapper.selectHotPostList(limit != null ? limit : 10);
        }

        Map<Long, Double> userSimilarities = calculateUserSimilarities(userId, userPostMatrix);
        List<Integer> recommendedPostIds = generateRecommendations(userId, userPostMatrix, userSimilarities, limit);

        if (recommendedPostIds.isEmpty()) {
            return postMapper.selectHotPostList(limit != null ? limit : 10);
        }

        return recommendedPostIds.stream()
                .map(postMapper::selectPostById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 用户行为数据结构（原有）
     */
    private static class UserBehavior {
        Long userId;
        Integer postId;
        Double score;
        String behaviorType;

        public UserBehavior(Long userId, Integer postId, Double score, String behaviorType) {
            this.userId = userId;
            this.postId = postId;
            this.score = score;
            this.behaviorType = behaviorType;
        }
    }

    /**
     * 收集历史用户行为数据（原有方法）
     */
    private List<UserBehavior> collectUserBehaviors() {
        List<UserBehavior> behaviors = new ArrayList<>();

        List<Map<String, Object>> likes = postMapper.selectAllPostLikes();
        for (Map<String, Object> like : likes) {
            behaviors.add(new UserBehavior(
                    ((Number) like.get("user_id")).longValue(),
                    ((Number) like.get("post_id")).intValue(),
                    3.0,
                    "like"
            ));
        }

        List<Map<String, Object>> comments = postMapper.selectAllComments();
        for (Map<String, Object> comment : comments) {
            behaviors.add(new UserBehavior(
                    ((Number) comment.get("user_id")).longValue(),
                    ((Number) comment.get("post_id")).intValue(),
                    2.0,
                    "comment"
            ));
        }

        List<Post> posts = postMapper.selectAllPosts();
        for (Post post : posts) {
            behaviors.add(new UserBehavior(
                    post.getUserId(),
                    post.getPostId(),
                    1.0,
                    "post"
            ));
        }

        return behaviors;
    }

    private Map<Long, Map<Integer, Double>> buildUserPostMatrix(List<UserBehavior> behaviors) {
        Map<Long, Map<Integer, Double>> matrix = new HashMap<>();
        for (UserBehavior behavior : behaviors) {
            matrix.computeIfAbsent(behavior.userId, k -> new HashMap<>())
                    .merge(behavior.postId, behavior.score, Double::sum);
        }
        return matrix;
    }

    private Map<Long, Double> calculateUserSimilarities(Long targetUserId,
                                                        Map<Long, Map<Integer, Double>> userPostMatrix) {
        Map<Long, Double> similarities = new HashMap<>();
        Map<Integer, Double> targetUserRatings = userPostMatrix.get(targetUserId);

        for (Map.Entry<Long, Map<Integer, Double>> entry : userPostMatrix.entrySet()) {
            Long userId = entry.getKey();
            if (userId.equals(targetUserId)) continue;

            Map<Integer, Double> userRatings = entry.getValue();
            double similarity = calculateCosineSimilarity(targetUserRatings, userRatings);

            if (similarity > 0) {
                similarities.put(userId, similarity);
            }
        }
        return similarities;
    }

    private double calculateCosineSimilarity(Map<Integer, Double> ratings1, Map<Integer, Double> ratings2) {
        Set<Integer> commonItems = new HashSet<>(ratings1.keySet());
        commonItems.retainAll(ratings2.keySet());

        if (commonItems.isEmpty()) return 0.0;

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Integer item : commonItems) {
            double rating1 = ratings1.get(item);
            double rating2 = ratings2.get(item);

            dotProduct += rating1 * rating2;
            norm1 += rating1 * rating1;
            norm2 += rating2 * rating2;
        }

        if (norm1 == 0.0 || norm2 == 0.0) return 0.0;

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private List<Integer> generateRecommendations(Long targetUserId,
                                                  Map<Long, Map<Integer, Double>> userPostMatrix,
                                                  Map<Long, Double> userSimilarities,
                                                  Integer limit) {
        Map<Integer, Double> recommendations = new HashMap<>();
        Map<Integer, Double> targetUserRatings = userPostMatrix.get(targetUserId);

        List<Map.Entry<Long, Double>> sortedSimilarUsers = userSimilarities.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(50)
                .collect(Collectors.toList());

        for (Map.Entry<Long, Double> entry : sortedSimilarUsers) {
            Long similarUserId = entry.getKey();
            Double similarity = entry.getValue();
            Map<Integer, Double> similarUserRatings = userPostMatrix.get(similarUserId);

            for (Map.Entry<Integer, Double> ratingEntry : similarUserRatings.entrySet()) {
                Integer postId = ratingEntry.getKey();
                Double rating = ratingEntry.getValue();

                if (!targetUserRatings.containsKey(postId)) {
                    recommendations.merge(postId, similarity * rating, Double::sum);
                }
            }
        }

        return recommendations.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(limit != null ? limit : 10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getContentBasedRecommendations(Long userId, Integer limit) {
        List<Integer> userPreferredSections = postMapper.selectUserPreferredSections(userId, 5);

        if (userPreferredSections.isEmpty()) {
            return postMapper.selectHotPostList(limit != null ? limit : 10);
        }

        List<Post> recommendations = new ArrayList<>();
        for (Integer sectionId : userPreferredSections) {
            List<Post> sectionPosts = postMapper.selectHotPostsBySectionId(sectionId, 3);
            recommendations.addAll(sectionPosts);
        }

        return recommendations.stream()
                .filter(post -> !postMapper.hasUserInteracted(userId, post.getPostId()))
                .distinct()
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getHybridRecommendations(Long userId, Integer limit) {
        int effectiveLimit = Math.max(limit != null ? limit : 10, 50);

        // 根据用户活跃度调整推荐策略权重
        UserActivity userActivity = getUserActivity(userId);
        double collaborativeWeight = calculateCollaborativeWeight(userActivity);
        double contentWeight = 1.0 - collaborativeWeight;

        int collaborativeLimit = (int) (effectiveLimit * collaborativeWeight);
        int contentLimit = (int) (effectiveLimit * contentWeight);

        List<Post> collaborativeRecommendations = getRecommendedPosts(userId, collaborativeLimit + 20);
        List<Post> contentRecommendations = getContentBasedRecommendations(userId, contentLimit + 20);

        // 添加时间戳和用户ID作为随机种子，确保不同用户不同时间获得不同结果
        Random random = new Random(System.currentTimeMillis() + userId.hashCode());

        Set<Integer> seenPostIds = new HashSet<>();
        List<Post> hybridRecommendations = new ArrayList<>();

        // 动态权重交替添加推荐结果
        int maxSize = Math.max(collaborativeRecommendations.size(), contentRecommendations.size());

        for (int i = 0; i < maxSize && hybridRecommendations.size() < effectiveLimit; i++) {
            // 使用随机权重决定添加顺序
            boolean addCollaborativeFirst = random.nextDouble() < collaborativeWeight;

            if (addCollaborativeFirst && i < collaborativeRecommendations.size()) {
                Post post = collaborativeRecommendations.get(i);
                if (seenPostIds.add(post.getPostId())) {
                    hybridRecommendations.add(post);
                }
            }

            if (i < contentRecommendations.size() && hybridRecommendations.size() < effectiveLimit) {
                Post post = contentRecommendations.get(i);
                if (seenPostIds.add(post.getPostId())) {
                    hybridRecommendations.add(post);
                }
            }

            if (!addCollaborativeFirst && i < collaborativeRecommendations.size()
                    && hybridRecommendations.size() < effectiveLimit) {
                Post post = collaborativeRecommendations.get(i);
                if (seenPostIds.add(post.getPostId())) {
                    hybridRecommendations.add(post);
                }
            }
        }

        // 添加一些随机的热门内容确保多样性
        if (hybridRecommendations.size() < effectiveLimit) {
            List<Post> hotPosts = postMapper.selectHotPostList(effectiveLimit);
            Collections.shuffle(hotPosts, random); // 随机打散热门内容

            for (Post post : hotPosts) {
                if (seenPostIds.add(post.getPostId()) && hybridRecommendations.size() < effectiveLimit) {
                    hybridRecommendations.add(post);
                }
            }
        }

        // 最终随机打散结果，增加多样性
        Collections.shuffle(hybridRecommendations, random);

        return hybridRecommendations;
    }

    /**
     * 获取用户活跃度信息 - 新增方法
     */
    private UserActivity getUserActivity(Long userId) {
        List<UserRecentBehavior> behaviors = getUserRecentBehaviors(userId);

        long recentLikes = behaviors.stream().filter(b -> "like".equals(b.behaviorType)).count();
        long recentComments = behaviors.stream().filter(b -> "comment".equals(b.behaviorType)).count();
        long recentPosts = behaviors.stream().filter(b -> "post".equals(b.behaviorType)).count();

        return new UserActivity(recentLikes, recentComments, recentPosts, behaviors.size());
    }

    /**
     * 计算协同过滤权重 - 新增方法
     */
    private double calculateCollaborativeWeight(UserActivity activity) {
        // 活跃用户更依赖协同过滤，新用户更依赖内容推荐
        if (activity.totalBehaviors < 5) {
            return 0.3; // 新用户，更多依赖内容推荐
        } else if (activity.totalBehaviors < 20) {
            return 0.5; // 中等活跃用户，平衡权重
        } else {
            return 0.7; // 高活跃用户，更多依赖协同过滤
        }
    }

    /**
     * 用户活跃度数据结构 - 新增类
     */
    private static class UserActivity {
        long recentLikes;
        long recentComments;
        long recentPosts;
        int totalBehaviors;

        public UserActivity(long likes, long comments, long posts, int total) {
            this.recentLikes = likes;
            this.recentComments = comments;
            this.recentPosts = posts;
            this.totalBehaviors = total;
        }
    }
}