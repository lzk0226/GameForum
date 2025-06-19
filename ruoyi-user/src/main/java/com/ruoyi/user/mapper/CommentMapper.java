package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper接口
 *
 * @author SockLightDust
 * @date 2025-05-27
 */
public interface CommentMapper {
    /**
     * 查询评论
     *
     * @param commentId 评论主键
     * @return 评论
     */
    public Comment selectCommentByCommentId(Integer commentId);

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
     * 根据帖子ID查询评论列表（仅查询正常状态的评论）
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID（用于查询点赞状态，可为null）
     * @return 评论集合
     */
    public List<Comment> selectCommentListByPostId(@Param("postId") Integer postId, @Param("userId") Long userId);

    /**
     * 根据用户ID查询评论列表（仅查询该用户发表的正常评论）
     *
     * @param userId 用户ID
     * @return 评论集合
     */
    public List<Comment> selectCommentListByUserId(Long userId);

    /**
     * 根据父评论ID查询子评论列表
     *
     * @param parentId 父评论ID
     * @param userId   当前用户ID（用于查询点赞状态，可为null）
     * @return 评论集合
     */
    public List<Comment> selectCommentListByParentId(@Param("parentId") Integer parentId, @Param("userId") Long userId);

    /**
     * 查询热门评论列表（按点赞数排序）
     *
     * @param postId 帖子ID
     * @param limit  限制数量
     * @return 评论集合
     */
    public List<Comment> selectHotCommentList(@Param("postId") Integer postId, @Param("limit") Integer limit);

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
     * 删除评论（逻辑删除，设置状态为1）
     *
     * @param commentId 评论主键
     * @param updateBy  更新者
     * @return 结果
     */
    public int hideComment(@Param("commentId") Integer commentId, @Param("updateBy") String updateBy);

    /**
     * 批量删除评论（逻辑删除，设置状态为1）
     *
     * @param commentIds 需要删除的评论主键集合
     * @param updateBy   更新者
     * @return 结果
     */
    public int hideComments(@Param("commentIds") Integer[] commentIds, @Param("updateBy") String updateBy);

    /**
     * 删除评论（物理删除，仅管理端使用）
     *
     * @param commentId 评论主键
     * @return 结果
     */
    public int deleteCommentByCommentId(Integer commentId);

    /**
     * 批量删除评论（物理删除，仅管理端使用）
     *
     * @param commentIds 需要删除的评论主键集合
     * @return 结果
     */
    public int deleteCommentByCommentIds(Integer[] commentIds);

    /**
     * 检查用户是否对评论点过赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 是否点赞
     */
    public boolean hasUserLikedComment(@Param("userId") Long userId, @Param("commentId") Integer commentId);

    /**
     * 用户对评论点赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 结果
     */
    public int likeComment(@Param("userId") Long userId, @Param("commentId") Integer commentId);

    /**
     * 用户取消对评论的点赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 结果
     */
    public int unlikeComment(@Param("userId") Long userId, @Param("commentId") Integer commentId);

    /**
     * 更新评论点赞数（+1）
     *
     * @param commentId 评论ID
     * @return 结果
     */
    public int incrementCommentLikeCount(Integer commentId);

    /**
     * 更新评论点赞数（-1）
     *
     * @param commentId 评论ID
     * @return 结果
     */
    public int decrementCommentLikeCount(Integer commentId);

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