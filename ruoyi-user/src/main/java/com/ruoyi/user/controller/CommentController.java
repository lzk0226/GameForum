package com.ruoyi.user.controller;

import com.ruoyi.user.R.R;
import com.ruoyi.user.domain.Comment;
import com.ruoyi.user.emun.ResultCodeEnum;
import com.ruoyi.user.service.ICommentService;
import com.ruoyi.user.service.IPostService;
import com.ruoyi.user.utils.JwtUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 论坛评论控制器
 *
 * @author SockLightDust
 * @date 2025-05-27
 */
@RestController
@RequestMapping("/user/comment")
@Validated
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IPostService postService;

    @Autowired
    private JwtUtils jwtUtils;

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
     * 获取评论详细信息
     */
    @GetMapping(value = "/{commentId}")
    public R<Comment> getInfo(@PathVariable("commentId") @NotNull(message = "评论ID不能为空") Integer commentId) {
        Comment comment = commentService.selectCommentDetail(commentId);
        if (comment == null) {
            return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND);
        }

        // 用户端只能查看正常状态的评论
        if (!"0".equals(comment.getStatus()) || "2".equals(comment.getDelFlag())) {
            return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND);
        }

        return R.ok(comment);
    }

    /**
     * 根据帖子ID查询评论列表（树形结构）
     */
    @GetMapping("/post/{postId}")
    public R<List<Comment>> listByPostId(
            @PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // 验证帖子是否存在
        if (postService.selectPostById(postId) == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 获取用户ID（用于判断是否点赞）
        Long userId = validateTokenAndGetUserId(authHeader);

        List<Comment> list = commentService.selectCommentListByPostId(postId, userId);
        return R.ok(list);
    }

    /**
     * 根据用户ID查询评论列表
     */
    @GetMapping("/user/{userId}")
    public R<List<Comment>> listByUserId(@PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        List<Comment> list = commentService.selectCommentListByUserId(userId);
        return R.ok(list);
    }

    /**
     * 查询当前用户的评论列表
     */
    @GetMapping("/my")
    public R<List<Comment>> myCommentList(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        List<Comment> list = commentService.selectCommentListByUserId(userId);
        return R.ok(list);
    }

    /**
     * 查询热门评论列表
     */
    @GetMapping("/hot/{postId}")
    public R<List<Comment>> hotList(
            @PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        // 验证帖子是否存在
        if (postService.selectPostById(postId) == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        List<Comment> list = commentService.selectHotCommentList(postId, limit);
        return R.ok(list);
    }

    /**
     * 统计帖子评论数量
     */
    @GetMapping("/count/{postId}")
    public R<Integer> countByPostId(@PathVariable("postId") @NotNull(message = "帖子ID不能为空") Integer postId) {
        // 验证帖子是否存在
        if (postService.selectPostById(postId) == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        int count = commentService.countCommentsByPostId(postId);
        return R.ok(count);
    }

    /**
     * 新增评论
     */
    @PostMapping
    public R<?> add(@Valid @RequestBody Comment comment,
                    @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证必要字段
        if (StringUtils.isEmpty(comment.getCommentContent())) {
            return R.fail(ResultCodeEnum.COMMENT_CONTENT_EMPTY);
        }
        if (comment.getPostId() == null) {
            return R.fail(ResultCodeEnum.POST_ID_EMPTY);
        }

        // 验证评论内容长度
        if (comment.getCommentContent().length() > 5000) {
            return R.fail(ResultCodeEnum.COMMENT_CONTENT_TOO_LONG);
        }

        // 验证帖子是否存在且状态正常
        if (postService.selectPostById(comment.getPostId()) == null) {
            return R.fail(ResultCodeEnum.POST_NOT_FOUND);
        }

        // 如果是回复评论，验证父评论是否存在
        if (comment.getParentId() != null) {
            Comment parentComment = commentService.selectCommentById(comment.getParentId());
            if (parentComment == null) {
                return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND, "父评论不存在");
            }
            if (!"0".equals(parentComment.getStatus()) || "2".equals(parentComment.getDelFlag())) {
                return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND, "父评论不可用");
            }
            // 验证父评论是否属于同一帖子
            if (!comment.getPostId().equals(parentComment.getPostId())) {
                return R.fail(ResultCodeEnum.VALIDATION_ERROR, "父评论和子评论必须在同一帖子下");
            }
        }

        // 设置用户信息
        comment.setUserId(userId);
        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        comment.setCreateBy(userName);

        int result = commentService.insertComment(comment);
        return result > 0 ? R.ok("评论发布成功") : R.fail("评论发布失败");
    }

    /**
     * 修改评论
     */
    @PutMapping
    public R<?> edit(@Valid @RequestBody Comment comment,
                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (comment.getCommentId() == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "评论ID不能为空");
        }

        // 验证评论是否存在
        Comment existComment = commentService.selectCommentById(comment.getCommentId());
        if (existComment == null) {
            return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND);
        }

        // 检查评论状态
        if ("2".equals(existComment.getDelFlag())) {
            return R.fail(ResultCodeEnum.COMMENT_ALREADY_DELETED);
        }
        if (!"0".equals(existComment.getStatus())) {
            return R.fail(ResultCodeEnum.COMMENT_STATUS_DISABLED);
        }

        // 验证用户权限（只能修改自己的评论）
        if (!userId.equals(existComment.getUserId())) {
            return R.fail(ResultCodeEnum.COMMENT_PERMISSION_DENIED);
        }

        // 验证评论内容
        if (StringUtils.isNotEmpty(comment.getCommentContent()) && comment.getCommentContent().trim().isEmpty()) {
            return R.fail(ResultCodeEnum.COMMENT_CONTENT_EMPTY);
        }
        if (StringUtils.isNotEmpty(comment.getCommentContent()) && comment.getCommentContent().length() > 5000) {
            return R.fail(ResultCodeEnum.COMMENT_CONTENT_TOO_LONG);
        }

        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        comment.setUpdateBy(userName);

        int result = commentService.updateComment(comment);
        return result > 0 ? R.ok("修改成功") : R.fail("修改失败");
    }

    /**
     * 隐藏评论（用户端的"删除"操作，实际是设置状态为停用）
     */
    @DeleteMapping("/{commentId}")
    public R<?> hide(@PathVariable("commentId") @NotNull(message = "评论ID不能为空") Integer commentId,
                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证评论是否存在
        Comment existComment = commentService.selectCommentById(commentId);
        if (existComment == null) {
            return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND);
        }

        // 检查评论状态
        if ("2".equals(existComment.getDelFlag())) {
            return R.fail(ResultCodeEnum.COMMENT_ALREADY_DELETED);
        }
        if (!"0".equals(existComment.getStatus())) {
            return R.fail(ResultCodeEnum.COMMENT_ALREADY_DELETED, "评论已被停用");
        }

        // 验证用户权限（只能删除自己的评论）
        if (!userId.equals(existComment.getUserId())) {
            return R.fail(ResultCodeEnum.COMMENT_PERMISSION_DENIED);
        }

        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        int result = commentService.hideComment(commentId, userName);
        return result > 0 ? R.ok("删除成功") : R.fail("删除失败");
    }

    /**
     * 批量隐藏评论
     */
    @DeleteMapping("/batch")
    public R<?> hideBatch(@RequestBody Integer[] commentIds,
                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (commentIds == null || commentIds.length == 0) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "请选择要删除的评论");
        }

        // 验证所有评论的权限
        for (Integer commentId : commentIds) {
            Comment existComment = commentService.selectCommentById(commentId);
            if (existComment == null) {
                return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND, "评论ID " + commentId + " 不存在");
            }
            if ("2".equals(existComment.getDelFlag())) {
                return R.fail(ResultCodeEnum.COMMENT_ALREADY_DELETED, "评论ID " + commentId + " 已被删除");
            }
            if (!"0".equals(existComment.getStatus())) {
                return R.fail(ResultCodeEnum.COMMENT_ALREADY_DELETED, "评论ID " + commentId + " 已被停用");
            }
            if (!userId.equals(existComment.getUserId())) {
                return R.fail(ResultCodeEnum.COMMENT_PERMISSION_DENIED, "您没有权限删除评论ID " + commentId);
            }
        }

        String userName = jwtUtils.getUserNameFromToken(authHeader.substring(7));
        int result = commentService.hideComments(commentIds, userName);
        return result > 0 ? R.ok("批量删除成功") : R.fail("批量删除失败");
    }

    /**
     * 点赞评论
     */
    @PostMapping("/like/{commentId}")
    public R<?> like(@PathVariable("commentId") @NotNull(message = "评论ID不能为空") Integer commentId,
                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证评论是否存在
        Comment existComment = commentService.selectCommentById(commentId);
        if (existComment == null) {
            return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND);
        }

        // 检查评论状态
        if ("2".equals(existComment.getDelFlag())) {
            return R.fail(ResultCodeEnum.COMMENT_ALREADY_DELETED);
        }
        if (!"0".equals(existComment.getStatus())) {
            return R.fail(ResultCodeEnum.COMMENT_STATUS_DISABLED);
        }

        // 检查用户是否已经点赞过
        boolean hasLiked = commentService.hasUserLikedComment(userId, commentId);
        if (hasLiked) {
            return R.fail(ResultCodeEnum.COMMENT_ALREADY_LIKED, "您已经点赞过该评论");
        }

        // 不能给自己的评论点赞
        if (userId.equals(existComment.getUserId())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "不能给自己的评论点赞");
        }

        // 执行点赞操作
        int result = commentService.likeComment(userId, commentId);
        return result > 0 ? R.ok("点赞成功") : R.fail("点赞失败");
    }

    /**
     * 取消点赞评论
     */
    @DeleteMapping("/like/{commentId}")
    public R<?> unlike(@PathVariable("commentId") @NotNull(message = "评论ID不能为空") Integer commentId,
                       @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证评论是否存在
        Comment existComment = commentService.selectCommentById(commentId);
        if (existComment == null) {
            return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND);
        }

        // 检查评论状态
        if ("2".equals(existComment.getDelFlag())) {
            return R.fail(ResultCodeEnum.COMMENT_ALREADY_DELETED);
        }
        if (!"0".equals(existComment.getStatus())) {
            return R.fail(ResultCodeEnum.COMMENT_STATUS_DISABLED);
        }

        // 检查用户是否已经点赞过
        boolean hasLiked = commentService.hasUserLikedComment(userId, commentId);
        if (!hasLiked) {
            return R.fail(ResultCodeEnum.COMMENT_NOT_LIKED, "您还没有点赞该评论");
        }

        // 执行取消点赞操作
        int result = commentService.unlikeComment(userId, commentId);
        return result > 0 ? R.ok("取消点赞成功") : R.fail("取消点赞失败");
    }

    /**
     * 检查用户是否点赞了某个评论
     */
    @GetMapping("/like/check/{commentId}")
    public R<Boolean> checkLikeStatus(@PathVariable("commentId") @NotNull(message = "评论ID不能为空") Integer commentId,
                                      @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 验证token
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证评论是否存在
        Comment existComment = commentService.selectCommentById(commentId);
        if (existComment == null) {
            return R.fail(ResultCodeEnum.COMMENT_NOT_FOUND);
        }

        boolean hasLiked = commentService.hasUserLikedComment(userId, commentId);
        return R.ok(hasLiked);
    }

    /**
     * 获取用户的评论统计信息
     */
    @GetMapping("/stats/{userId}")
    public R<Comment> getUserCommentStats(@PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        Comment stats = commentService.selectUserCommentStats(userId);
        return R.ok(stats);
    }

    /**
     * 获取当前用户的评论统计信息
     */
    @GetMapping("/stats/my")
    public R<Comment> getMyCommentStats(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        Comment stats = commentService.selectUserCommentStats(userId);
        return R.ok(stats);
    }
}