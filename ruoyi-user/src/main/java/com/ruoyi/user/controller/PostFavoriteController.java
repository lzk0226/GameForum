package com.ruoyi.user.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.R.R;
import com.ruoyi.user.domain.Post;
import com.ruoyi.user.domain.PostFavorite;
import com.ruoyi.user.emun.ResultCodeEnum;
import com.ruoyi.user.service.IPostFavoriteService;
import com.ruoyi.user.service.IPostService;
import com.ruoyi.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 帖子收藏 Controller
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
@RestController
@RequestMapping("/user/post/favorite")
@Validated
public class PostFavoriteController {

    @Autowired
    private IPostFavoriteService postFavoriteService;

    @Autowired
    private IPostService postService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 验证 Token 并获取用户 ID
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
     * 获取当前用户的收藏列表
     */
    @GetMapping("/my")
    public R<List<PostFavorite>> getMyFavorites(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        List<PostFavorite> favorites = postFavoriteService.selectFavoriteListByUserId(userId);
        return R.ok(favorites);
    }

    /**
     * 获取指定用户的收藏列表
     */
    @GetMapping("/user/{userId}")
    public R<List<PostFavorite>> getUserFavorites(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        List<PostFavorite> favorites = postFavoriteService.selectFavoriteListByUserId(userId);
        return R.ok(favorites);
    }

    /**
     * 获取帖子的收藏用户列表
     */
    @GetMapping("/post/{postId}")
    public R<List<PostFavorite>> getPostFavorites(
            @PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId) {
        Post post = postService.selectPostById(postId);
        if (post == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        List<PostFavorite> favorites = postFavoriteService.selectFavoriteListByPostId(postId);
        return R.ok(favorites);
    }

    /**
     * 收藏帖子
     */
    @PostMapping("/{postId}")
    public R<?> addFavorite(
            @PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 检查帖子是否存在
        Post post = postService.selectPostById(postId);
        if (post == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 检查帖子状态
        if ("2".equals(post.getDelFlag())) {
            return R.fail(ResultCodeEnum.POST_ALREADY_DELETED);
        }
        if (!"0".equals(post.getStatus())) {
            return R.fail(ResultCodeEnum.POST_STATUS_DISABLED);
        }

        // 检查是否已收藏
        if (postFavoriteService.checkUserFavorited(userId, postId)) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "您已经收藏过该帖子");
        }

        // 添加收藏
        int result = postFavoriteService.addFavorite(userId, postId);
        return result > 0 ? R.ok("收藏成功") : R.fail("收藏失败");
    }

    /**
     * 取消收藏帖子
     */
    @DeleteMapping("/{postId}")
    public R<?> removeFavorite(
            @PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 检查帖子是否存在
        Post post = postService.selectPostById(postId);
        if (post == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 检查是否已收藏
        if (!postFavoriteService.checkUserFavorited(userId, postId)) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "您还没有收藏该帖子");
        }

        // 取消收藏
        int result = postFavoriteService.removeFavorite(userId, postId);
        return result > 0 ? R.ok("取消收藏成功") : R.fail("取消收藏失败");
    }

    /**
     * 批量取消收藏
     */
    @DeleteMapping("/batch")
    public R<?> removeFavoritesBatch(
            @RequestBody List<Integer> postIds,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (postIds == null || postIds.isEmpty()) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "请选择要取消收藏的帖子");
        }

        int result = postFavoriteService.removeFavoriteByIds(userId, postIds);
        return result > 0 ? R.ok("批量取消收藏成功") : R.fail("批量取消收藏失败");
    }

    /**
     * 检查用户是否收藏了某个帖子
     */
    @GetMapping("/check/{postId}")
    public R<Boolean> checkFavoriteStatus(
            @PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 检查帖子是否存在
        Post post = postService.selectPostById(postId);
        if (post == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        boolean hasFavorited = postFavoriteService.checkUserFavorited(userId, postId);
        return R.ok(hasFavorited);
    }

    /**
     * 批量检查用户是否收藏了指定帖子
     */
    @PostMapping("/check/batch")
    public R<?> checkFavoriteStatusBatch(
            @RequestBody List<Integer> postIds,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (postIds == null || postIds.isEmpty()) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "帖子ID列表不能为空");
        }

        Map<Integer, Boolean> favoriteStatus = postFavoriteService.checkUserFavoritedBatch(userId, postIds);
        return R.ok(favoriteStatus);
    }

    /**
     * 获取当前用户的收藏数量
     */
    @GetMapping("/count/my")
    public R<Integer> getMyFavoriteCount(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        int count = postFavoriteService.countFavoriteByUserId(userId);
        return R.ok(count);
    }

    /**
     * 获取指定用户的收藏数量
     */
    @GetMapping("/count/user/{userId}")
    public R<Integer> getUserFavoriteCount(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        int count = postFavoriteService.countFavoriteByUserId(userId);
        return R.ok(count);
    }

    /**
     * 获取帖子的收藏数量
     */
    @GetMapping("/count/post/{postId}")
    public R<Integer> getPostFavoriteCount(
            @PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId) {
        Post post = postService.selectPostById(postId);
        if (post == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        int count = postFavoriteService.countFavoriteByPostId(postId);
        return R.ok(count);
    }
}