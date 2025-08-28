package com.ruoyi.user.service;

import com.ruoyi.user.domain.Post;
import java.util.List;

/**
 * 帖子推荐服务接口
 */
public interface IPostRecommendationService {

    /**
     * 获取基于协同过滤的个性化推荐帖子
     * @param userId 用户ID
     * @param limit 推荐数量限制
     * @return 推荐帖子列表
     */
    List<Post> getRecommendedPosts(Long userId, Integer limit);

    /**
     * 获取基于内容的推荐帖子
     * @param userId 用户ID
     * @param limit 推荐数量限制
     * @return 推荐帖子列表
     */
    List<Post> getContentBasedRecommendations(Long userId, Integer limit);

    /**
     * 获取混合推荐帖子（协同过滤 + 基于内容）
     * @param userId 用户ID
     * @param limit 推荐数量限制
     * @return 推荐帖子列表
     */
    List<Post> getHybridRecommendations(Long userId, Integer limit);


    void recordUserBehavior(Long userId, Integer postId, String behaviorType, Integer sectionId);
    List<Post> getRecommendedPostsWithPaging(Long userId, Integer limit, Integer offset);
    List<Post> getHybridRecommendationsWithPaging(Long userId, Integer limit, Integer offset);

}