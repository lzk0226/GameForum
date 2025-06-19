package com.ruoyi.user.service;

import com.ruoyi.user.domain.Comment;

import java.util.List;

/**
 * 评论Service接口
 *
 * @author SockLightDust
 * @date 2025-05-27
 */
public interface ICommentService {
    /**
     * 查询评论
     *
     * @param commentId 评论主键
     * @return 评论
     */
    public Comment selectCommentById(Integer commentId);

    /**
     * 查询评论详情（包含用户信息等）
     *
     * @param commentId 评论主键
     * @return 评论
     */
    public Comment selectCommentDetail(Integer commentId);

    /**
     * 查询评论列表
     *
     * @param comment 评论
     * @return 评论集合
     */
    public List<Comment> selectCommentList(Comment comment);

    /**
     * 根据帖子ID查询评论列表（树形结构）
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID（用于查询点赞状态，可为null）
     * @return 评论集合
     */
    public List<Comment> selectCommentListByPostId(Integer postId, Long userId);

    /**
     * 根据用户ID查询评论列表
     *
     * @param userId 用户ID
     * @return 评论集合
     */
    public List<Comment> selectCommentListByUserId(Long userId);

    /**
     * 查询热门评论列表
     *
     * @param postId 帖子ID
     * @param limit  限制数量
     * @return 评论集合
     */
    public List<Comment> selectHotCommentList(Integer postId, Integer limit);

    /**
     * 新增评论
     *
     * @param comment 评论
     * @return 结果
     */
    public int insertComment(Comment comment);

    /**
     * 修改评论
     *
     * @param comment 评论
     * @return 结果
     */
    public int updateComment(Comment comment);

    /**
     * 隐藏评论（用户端删除，设置状态为1）
     *
     * @param commentId 评论主键
     * @param updateBy  更新者
     * @return 结果
     */
    public int hideComment(Integer commentId, String updateBy);

    /**
     * 批量隐藏评论
     *
     * @param commentIds 需要删除的评论主键集合
     * @param updateBy   更新者
     * @return 结果
     */
    public int hideComments(Integer[] commentIds, String updateBy);

    /**
     * 检查用户是否对评论点过赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 是否点赞
     */
    public boolean hasUserLikedComment(Long userId, Integer commentId);

    /**
     * 用户对评论点赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 结果
     */
    public int likeComment(Long userId, Integer commentId);

    /**
     * 用户取消对评论的点赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 结果
     */
    public int unlikeComment(Long userId, Integer commentId);

    /**
     * 查询用户评论统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     */
    public Comment selectUserCommentStats(Long userId);

    /**
     * 统计帖子下的评论数量
     *
     * @param postId 帖子ID
     * @return 评论数量
     */
    public int countCommentsByPostId(Integer postId);

    /**
     * 统计用户发表的评论数量
     *
     * @param userId 用户ID
     * @return 评论数量
     */
    public int countCommentsByUserId(Long userId);
}