package com.ruoyi.user.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.R.R;
import com.ruoyi.user.domain.Post;
import com.ruoyi.user.emun.ResultCodeEnum;
import com.ruoyi.user.service.IPostRecommendationService;
import com.ruoyi.user.service.IPostService;
import com.ruoyi.user.service.IUserService;
import com.ruoyi.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 论坛帖子控制器
 *
 * @author SockLightDust
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/user/post")
@Validated
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private FileUploadController fileUploadController;

    // 图片文件扩展名正则表达式
    private static final Pattern IMAGE_PATTERN = Pattern.compile(".*\\.(jpg|jpeg|png|gif|bmp|webp|svg)$", Pattern.CASE_INSENSITIVE);

    /**
     * 验证token并获取用户ID
     */
    private Long validateTokenAndGetUserId(String authHeader) {
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return null;
        }

        return jwtUtils.getUserIdFromToken(token);
    }

    /**
     * 验证图片路径格式
     */
    private boolean isValidImagePath(String photo) {
        if (StringUtils.isEmpty(photo)) {
            return true; // 允许为空
        }

        // 验证路径格式和文件扩展名
        return IMAGE_PATTERN.matcher(photo).matches();
    }

    /**
     * 帖子详情
     */
    @GetMapping(value = "/{postId}")
    public R<Post> getInfo(@PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId) {
        Post post = postService.selectPostDetail(postId);
        if (post == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 将帖子内容中的图片路径转换为base64
        if (StringUtils.isNotEmpty(post.getPostContent())) {
            String processedContent = fileUploadController.convertImagePathsToBase64(post.getPostContent());
            post.setPostContent(processedContent);
        }

        return R.ok(post);
    }

    /**
     * 计算纯文本内容的长度（不包含base64图片和HTML标签）
     */
    private Long calculatePureTextLength(String htmlContent) {
        if (StringUtils.isEmpty(htmlContent)) {
            return 0L;
        }

        // 移除base64图片
        String withoutBase64 = htmlContent.replaceAll(
                "<img[^>]+src=\"data:image/\\w+;base64,[^\"]+\"[^>]*>",
                ""
        );

        // 移除所有HTML标签
        String plainText = withoutBase64.replaceAll("<[^>]+>", "");

        // 解码HTML实体
        plainText = plainText.replace("&nbsp;", " ")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'");

        return (long) plainText.length();
    }

    /**
     * 验证纯文本内容长度
     */
    private boolean validateContentLength(String htmlContent, long maxLength) {
        Long pureTextLength = calculatePureTextLength(htmlContent);
        return pureTextLength <= maxLength;
    }

    // ============ 在新增帖子中使用 ============
    @PostMapping
    public R<?> add(@Valid @RequestBody Post post,
                    @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证必要字段
        if (StringUtils.isEmpty(post.getPostTitle())) {
            return R.fail(ResultCodeEnum.POST_TITLE_EMPTY);
        }
        if (StringUtils.isEmpty(post.getPostContent())) {
            return R.fail(ResultCodeEnum.POST_CONTENT_EMPTY);
        }
        if (post.getSectionId() == null) {
            return R.fail(ResultCodeEnum.POST_SECTION_EMPTY);
        }

        // 验证标题长度
        if (post.getPostTitle().length() > 100) {
            return R.fail(ResultCodeEnum.POST_TITLE_TOO_LONG);
        }

        // 处理富文本内容：提取base64图片并保存
        Map<String, Object> processResult = fileUploadController.processRichTextContent(
                post.getPostContent(), authHeader
        );

        String processedContent = (String) processResult.get("content");
        String firstImagePath = (String) processResult.get("firstImagePath");

        // 验证纯文本内容长度（不计入base64和HTML标签）
        // 这里设置为100000字符的纯文本内容限制
        if (!validateContentLength(processedContent, 100000L)) {
            return R.fail(ResultCodeEnum.POST_CONTENT_TOO_LONG);
        }

        // 设置处理后的内容
        post.setPostContent(processedContent);

        // 设置第一张图片作为photo字段
        if (firstImagePath != null) {
            post.setPhoto(firstImagePath);
        }

        // 设置用户信息
        post.setUserId(userId);
        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        post.setCreateBy(userName);

        int result = postService.insertPost(post);
        return result > 0 ? R.ok("发布成功") : R.fail("发布失败");
    }

    // ============ 在修改帖子中使用 ============
    @PutMapping
    public R<?> edit(@Valid @RequestBody Post post,
                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (post.getPostId() == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "帖子ID不能为空");
        }

        // 验证帖子是否存在
        Post existPost = postService.selectPostById(post.getPostId());
        if (existPost == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 检查帖子状态
        if ("2".equals(existPost.getDelFlag())) {
            return R.fail(ResultCodeEnum.POST_ALREADY_DELETED);
        }
        if (!"0".equals(existPost.getStatus())) {
            return R.fail(ResultCodeEnum.POST_STATUS_DISABLED);
        }

        // 验证用户权限（只能修改自己的帖子）
        if (!userId.equals(existPost.getUserId())) {
            return R.fail(ResultCodeEnum.POST_PERMISSION_DENIED);
        }

        // 验证必要字段
        if (StringUtils.isNotEmpty(post.getPostTitle()) && post.getPostTitle().trim().isEmpty()) {
            return R.fail(ResultCodeEnum.POST_TITLE_EMPTY);
        }
        if (StringUtils.isNotEmpty(post.getPostContent()) && post.getPostContent().trim().isEmpty()) {
            return R.fail(ResultCodeEnum.POST_CONTENT_EMPTY);
        }

        // 验证标题长度
        if (StringUtils.isNotEmpty(post.getPostTitle()) && post.getPostTitle().length() > 100) {
            return R.fail(ResultCodeEnum.POST_TITLE_TOO_LONG);
        }

        // 如果更新了内容，处理富文本
        if (StringUtils.isNotEmpty(post.getPostContent())) {
            Map<String, Object> processResult = fileUploadController.processRichTextContent(
                    post.getPostContent(), authHeader
            );

            String processedContent = (String) processResult.get("content");
            String firstImagePath = (String) processResult.get("firstImagePath");

            // 验证纯文本内容长度（不计入base64和HTML标签）
            if (!validateContentLength(processedContent, 100000L)) {
                return R.fail(ResultCodeEnum.POST_CONTENT_TOO_LONG);
            }

            post.setPostContent(processedContent);

            // 更新第一张图片
            if (firstImagePath != null) {
                post.setPhoto(firstImagePath);
            }
        }

        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        post.setUpdateBy(userName);

        int result = postService.updatePost(post);
        return result > 0 ? R.ok("修改成功") : R.fail("修改失败");
    }


    /**
     * 获取帖子基本信息（不增加浏览数）
     */
    @GetMapping(value = "/info/{postId}")
    public R<Post> getBasicInfo(@PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId) {
        Post post = postService.selectPostById(postId);
        if (post == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }
        return R.ok(post);
    }

    /**
     * 查询帖子列表
     */
    @GetMapping("/list")
    public R<List<Post>> list(Post post) {
        List<Post> list = postService.selectPostList(post);
        return R.ok(list);
    }

    /**
     * 根据版块ID查询帖子列表
     */
    @GetMapping("/section/{sectionId}")
    public R<List<Post>> listBySectionId(@PathVariable("sectionId") @NotNull(message = "版块ID不能为空") Integer sectionId) {
        List<Post> list = postService.selectPostListBySectionId(sectionId);
        return R.ok(list);
    }

    /**
     * 根据用户ID查询帖子列表
     */
    @GetMapping("/user/{userId}")
    public R<List<Post>> listByUserId(@PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        List<Post> list = postService.selectPostListByUserId(userId);
        return R.ok(list);
    }

    /**
     * 查询当前用户的帖子列表
     */
    @GetMapping("/my")
    public R<List<Post>> myPostList(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        List<Post> list = postService.selectPostListByUserId(userId);
        return R.ok(list);
    }

    /**
     * 根据帖子标题搜索帖子
     */
    @GetMapping("/search")
    public R<List<Post>> searchByTitle(@RequestParam("title") @NotBlank(message = "搜索关键词不能为空") String postTitle) {
        List<Post> list = postService.selectPostListByTitle(postTitle);
        return R.ok(list);
    }

    /**
     * 查询热门帖子列表
     */
    @GetMapping("/hot")
    public R<List<Post>> hotList(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Post> list = postService.selectHotPostList(limit);
        return R.ok(list);
    }

    /**
     * 查询置顶帖子列表
     */
    @GetMapping("/top")
    public R<List<Post>> topList(@RequestParam(value = "sectionId", required = false) Integer sectionId) {
        List<Post> list = postService.selectTopPostList(sectionId);
        return R.ok(list);
    }

    /**
     * 新增帖子
     */
    /*
    @PostMapping
    public R<?> add(@Valid @RequestBody Post post,
                    @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证必要字段
        if (StringUtils.isEmpty(post.getPostTitle())) {
            return R.fail(ResultCodeEnum.POST_TITLE_EMPTY);
        }
        if (StringUtils.isEmpty(post.getPostContent())) {
            return R.fail(ResultCodeEnum.POST_CONTENT_EMPTY);
        }
        if (post.getSectionId() == null) {
            return R.fail(ResultCodeEnum.POST_SECTION_EMPTY);
        }

        // 验证标题长度（假设最大100字符）
        if (post.getPostTitle().length() > 100) {
            return R.fail(ResultCodeEnum.POST_TITLE_TOO_LONG);
        }
        // 验证内容长度（假设最大10000字符）
        if (post.getPostContent().length() > 10000) {
            return R.fail(ResultCodeEnum.POST_CONTENT_TOO_LONG);
        }

        // 验证图片路径格式
        if (!isValidImagePath(post.getPhoto())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "图片路径格式不正确，请上传有效的图片文件");
        }

        // 设置用户信息
        post.setUserId(userId);
        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        post.setCreateBy(userName);

        int result = postService.insertPost(post);
        return result > 0 ? R.ok("发布成功") : R.fail("发布失败");
    }

    */
    /**
     * 修改帖子
     */
    /*
    @PutMapping
    public R<?> edit(@Valid @RequestBody Post post,
                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (post.getPostId() == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "帖子ID不能为空");
        }

        // 验证帖子是否存在
        Post existPost = postService.selectPostById(post.getPostId());
        if (existPost == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 检查帖子状态
        if ("2".equals(existPost.getDelFlag())) {
            return R.fail(ResultCodeEnum.POST_ALREADY_DELETED);
        }
        if (!"0".equals(existPost.getStatus())) {
            return R.fail(ResultCodeEnum.POST_STATUS_DISABLED);
        }

        // 验证用户权限（只能修改自己的帖子）
        if (!userId.equals(existPost.getUserId())) {
            return R.fail(ResultCodeEnum.POST_PERMISSION_DENIED);
        }

        // 验证必要字段
        if (StringUtils.isNotEmpty(post.getPostTitle()) && post.getPostTitle().trim().isEmpty()) {
            return R.fail(ResultCodeEnum.POST_TITLE_EMPTY);
        }
        if (StringUtils.isNotEmpty(post.getPostContent()) && post.getPostContent().trim().isEmpty()) {
            return R.fail(ResultCodeEnum.POST_CONTENT_EMPTY);
        }

        // 验证长度
        if (StringUtils.isNotEmpty(post.getPostTitle()) && post.getPostTitle().length() > 100) {
            return R.fail(ResultCodeEnum.POST_TITLE_TOO_LONG);
        }
        if (StringUtils.isNotEmpty(post.getPostContent()) && post.getPostContent().length() > 10000) {
            return R.fail(ResultCodeEnum.POST_CONTENT_TOO_LONG);
        }

        // 验证图片路径格式（如果提供了新的图片路径）
        if (!isValidImagePath(post.getPhoto())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "图片路径格式不正确，请上传有效的图片文件");
        }

        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        post.setUpdateBy(userName);

        int result = postService.updatePost(post);
        return result > 0 ? R.ok("修改成功") : R.fail("修改失败");
    }
*/
    /**
     * 隐藏帖子（用户端的"删除"操作，实际是逻辑删除）
     */
    @DeleteMapping("/{postId}")
    public R<?> hide(@PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证帖子是否存在
        Post existPost = postService.selectPostById(postId);
        if (existPost == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 检查帖子状态
        if ("2".equals(existPost.getDelFlag())) {
            return R.fail(ResultCodeEnum.POST_ALREADY_DELETED);
        }

        // 验证用户权限（只能删除自己的帖子）
        if (!userId.equals(existPost.getUserId())) {
            return R.fail(ResultCodeEnum.POST_PERMISSION_DENIED);
        }

        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        int result = postService.hidePost(postId, userName);
        return result > 0 ? R.ok("删除成功") : R.fail("删除失败");
    }

    /**
     * 批量隐藏帖子
     */
    @DeleteMapping("/batch")
    public R<?> hideBatch(@RequestBody Integer[] postIds,
                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (postIds == null || postIds.length == 0) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "请选择要删除的帖子");
        }

        // 验证所有帖子的权限
        for (Integer postId : postIds) {
            Post existPost = postService.selectPostById(postId);
            if (existPost == null) {
                return R.fail(ResultCodeEnum.POST_NOT_FOUND, "帖子ID " + postId + " 不存在");
            }
            if ("2".equals(existPost.getDelFlag())) {
                return R.fail(ResultCodeEnum.POST_ALREADY_DELETED, "帖子ID " + postId + " 已被删除");
            }
            if (!userId.equals(existPost.getUserId())) {
                return R.fail(ResultCodeEnum.POST_PERMISSION_DENIED, "您没有权限删除帖子ID " + postId);
            }
        }

        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        int result = postService.hidePosts(postIds, userName);
        return result > 0 ? R.ok("批量删除成功") : R.fail("批量删除失败");
    }

    /**
     * 点赞帖子 - 集成实时行为记录
     */
    @PostMapping("/like/{postId}")
    public R<?> like(@PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证帖子是否存在
        Post existPost = postService.selectPostById(postId);
        if (existPost == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 检查帖子状态
        if ("2".equals(existPost.getDelFlag())) {
            return R.fail(ResultCodeEnum.POST_ALREADY_DELETED);
        }
        if (!"0".equals(existPost.getStatus())) {
            return R.fail(ResultCodeEnum.POST_STATUS_DISABLED);
        }

        // 检查用户是否已经点赞过
        boolean hasLiked = postService.hasUserLikedPost(userId, postId);
        if (hasLiked) {
            return R.fail(ResultCodeEnum.POST_ALREADY_LIKED, "您已经点赞过该帖子");
        }

        // 不能给自己的帖子点赞
        if (userId.equals(existPost.getUserId())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "不能给自己的帖子点赞");
        }

        // 执行点赞操作
        int result = postService.likePost(userId, postId);

        if (result > 0) {
            // 记录实时行为
            recommendationService.recordUserBehavior(userId, postId, "like", existPost.getSectionId());
            return R.ok("点赞成功");
        } else {
            return R.fail("点赞失败");
        }
    }

    /**
     * 取消点赞帖子 - 集成实时行为记录
     */
    @DeleteMapping("/like/{postId}")
    public R<?> unlike(@PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
                       @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证帖子是否存在
        Post existPost = postService.selectPostById(postId);
        if (existPost == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 检查帖子状态
        if ("2".equals(existPost.getDelFlag())) {
            return R.fail(ResultCodeEnum.POST_ALREADY_DELETED);
        }
        if (!"0".equals(existPost.getStatus())) {
            return R.fail(ResultCodeEnum.POST_STATUS_DISABLED);
        }

        // 检查用户是否已经点赞过
        boolean hasLiked = postService.hasUserLikedPost(userId, postId);
        if (!hasLiked) {
            return R.fail(ResultCodeEnum.POST_NOT_LIKED, "您还没有点赞该帖子");
        }

        // 执行取消点赞操作
        int result = postService.unlikePost(userId, postId);

        if (result > 0) {
            // 记录取消点赞行为（负分）
            recommendationService.recordUserBehavior(userId, postId, "unlike", existPost.getSectionId());
            return R.ok("取消点赞成功");
        } else {
            return R.fail("取消点赞失败");
        }
    }

    /**
     * 检查用户是否点赞了某个帖子
     */
    @GetMapping("/like/check/{postId}")
    public R<Boolean> checkLikeStatus(@PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
                                      @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证帖子是否存在
        Post existPost = postService.selectPostById(postId);
        if (existPost == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        boolean hasLiked = postService.hasUserLikedPost(userId, postId);
        return R.ok(hasLiked);
    }

    /**
     * 获取用户的帖子统计信息
     */
    @GetMapping("/stats/{userId}")
    public R<Post> getUserPostStats(@PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        Post stats = postService.selectUserPostStats(userId);
        return R.ok(stats);
    }

    /**
     * 获取当前用户的帖子统计信息
     */
    @GetMapping("/stats/my")
    public R<Post> getMyPostStats(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        Post stats = postService.selectUserPostStats(userId);
        return R.ok(stats);
    }

    // ========== 个性化推荐接口 ==========

    /**
     * 获取个性化推荐帖子 - 基于协同过滤算法
     */
    @Autowired
    private IPostRecommendationService recommendationService;

    @GetMapping("/recommendations")
    public R<List<Post>> getPersonalizedRecommendations(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            // 未登录用户返回热门帖子
            List<Post> hotPosts = postService.selectHotPostList(limit);
            return R.ok(hotPosts);
        }

        try {
            List<Post> recommendations = recommendationService.getRecommendedPosts(userId, limit);
            return R.ok(recommendations);
        } catch (Exception e) {
            // 如果推荐算法出错，降级到热门帖子
            List<Post> hotPosts = postService.selectHotPostList(limit);
            return R.ok(hotPosts);
        }
    }

    /**
     * 获取基于内容的推荐帖子
     */
    @GetMapping("/recommendations/content")
    public R<List<Post>> getContentBasedRecommendations(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        try {
            List<Post> recommendations = recommendationService.getContentBasedRecommendations(userId, limit);
            return R.ok(recommendations);
        } catch (Exception e) {
            List<Post> hotPosts = postService.selectHotPostList(limit);
            return R.ok(hotPosts);
        }
    }

    /**
     * 获取混合推荐帖子 - 支持分页
     */
    @GetMapping("/recommendations/hybrid")
    public R<List<Post>> getHybridRecommendations(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        try {
            List<Post> recommendations = recommendationService.getHybridRecommendationsWithPaging(
                    userId, limit, page);
            return R.ok(recommendations);
        } catch (Exception e) {
            // 如果推荐算法出错，降级到热门帖子
            List<Post> hotPosts = postService.selectHotPostList(limit);
            return R.ok(hotPosts);
        }
    }

    /**
     * 获取推荐帖子的详细信息（包含推荐类型和原因）
     */
    @GetMapping("/recommendations/detailed")
    public R<Map<String, Object>> getDetailedRecommendations(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("posts", postService.selectHotPostList(limit));
            result.put("type", "hot");
            result.put("reason", "热门推荐");
            return R.ok(result);
        }

        try {
            Map<String, Object> result = new HashMap<>();

            // 获取不同类型的推荐
            List<Post> collaborativeRecommendations = recommendationService.getRecommendedPosts(userId, limit / 2);
            List<Post> contentRecommendations = recommendationService.getContentBasedRecommendations(userId, limit / 2);
            List<Post> hybridRecommendations = recommendationService.getHybridRecommendations(userId, limit);

            result.put("collaborative", collaborativeRecommendations);
            result.put("content", contentRecommendations);
            result.put("hybrid", hybridRecommendations);
            result.put("type", "personalized");
            result.put("reason", "基于您的兴趣偏好推荐");

            return R.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("posts", postService.selectHotPostList(limit));
            result.put("type", "hot");
            result.put("reason", "热门推荐");
            return R.ok(result);
        }
    }

}