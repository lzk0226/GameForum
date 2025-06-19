package com.ruoyi.user.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.user.domain.Comment;
import com.ruoyi.user.mapper.CommentMapper;
import com.ruoyi.user.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论Service业务层处理
 *
 * @author SockLightDust
 * @date 2025-05-27
 */
@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 查询评论
     *
     * @param commentId 评论主键
     * @return 评论
     */
    @Override
    public Comment selectCommentById(Integer commentId) {
        return commentMapper.selectCommentByCommentId(commentId);
    }

    /**
     * 查询评论详情
     *
     * @param commentId 评论主键
     * @return 评论
     */
    @Override
    public Comment selectCommentDetail(Integer commentId) {
        return commentMapper.selectCommentDetail(commentId);
    }

    /**
     * 查询评论列表
     *
     * @param comment 评论
     * @return 评论
     */
    @Override
    public List<Comment> selectCommentList(Comment comment) {
        return commentMapper.selectCommentList(comment);
    }

    /**
     * 根据帖子ID查询评论列表（树形结构）
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID
     * @return 评论集合
     */
    @Override
    public List<Comment> selectCommentListByPostId(Integer postId, Long userId) {
        List<Comment> allComments = commentMapper.selectCommentListByPostId(postId, userId);

        // 构建树形结构
        List<Comment> rootComments = new ArrayList<>();
        Map<Integer, List<Comment>> childrenMap = allComments.stream()
                .filter(comment -> comment.getParentId() != null)
                .collect(Collectors.groupingBy(Comment::getParentId));

        for (Comment comment : allComments) {
            if (comment.getParentId() == null) {
                // 根评论
                comment.setChildren(childrenMap.getOrDefault(comment.getCommentId(), new ArrayList<>()));
                comment.setChildrenCount(comment.getChildren().size());
                rootComments.add(comment);
            }
        }

        return rootComments;
    }

    /**
     * 根据用户ID查询评论列表
     *
     * @param userId 用户ID
     * @return 评论集合
     */
    @Override
    public List<Comment> selectCommentListByUserId(Long userId) {
        return commentMapper.selectCommentListByUserId(userId);
    }

    /**
     * 查询热门评论列表
     *
     * @param postId 帖子ID
     * @param limit  限制数量
     * @return 评论集合
     */
    @Override
    public List<Comment> selectHotCommentList(Integer postId, Integer limit) {
        return commentMapper.selectHotCommentList(postId, limit);
    }

    /**
     * 新增评论
     *
     * @param comment 评论
     * @return 结果
     */
    @Override
    public int insertComment(Comment comment) {
        comment.setCreateTime(DateUtils.getNowDate());
        return commentMapper.insertComment(comment);
    }

    /**
     * 修改评论
     *
     * @param comment 评论
     * @return 结果
     */
    @Override
    public int updateComment(Comment comment) {
        comment.setUpdateTime(DateUtils.getNowDate());
        return commentMapper.updateComment(comment);
    }

    /**
     * 隐藏评论
     *
     * @param commentId 评论主键
     * @param updateBy  更新者
     * @return 结果
     */
    @Override
    public int hideComment(Integer commentId, String updateBy) {
        return commentMapper.hideComment(commentId, updateBy);
    }

    /**
     * 批量隐藏评论
     *
     * @param commentIds 需要删除的评论主键集合
     * @param updateBy   更新者
     * @return 结果
     */
    @Override
    public int hideComments(Integer[] commentIds, String updateBy) {
        return commentMapper.hideComments(commentIds, updateBy);
    }

    /**
     * 检查用户是否对评论点过赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 是否点赞
     */
    @Override
    public boolean hasUserLikedComment(Long userId, Integer commentId) {
        return commentMapper.hasUserLikedComment(userId, commentId);
    }

    /**
     * 用户对评论点赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 结果
     */
    @Override
    @Transactional
    public int likeComment(Long userId, Integer commentId) {
        // 添加点赞记录
        int result = commentMapper.likeComment(userId, commentId);
        if (result > 0) {
            // 更新评论点赞数
            commentMapper.incrementCommentLikeCount(commentId);
        }
        return result;
    }

    /**
     * 用户取消对评论的点赞
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 结果
     */
    @Override
    @Transactional
    public int unlikeComment(Long userId, Integer commentId) {
        // 删除点赞记录
        int result = commentMapper.unlikeComment(userId, commentId);
        if (result > 0) {
            // 更新评论点赞数
            commentMapper.decrementCommentLikeCount(commentId);
        }
        return result;
    }

    /**
     * 查询用户评论统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     */
    @Override
    public Comment selectUserCommentStats(Long userId) {
        return commentMapper.selectUserCommentStats(userId);
    }

    /**
     * 统计帖子下的评论数量
     *
     * @param postId 帖子ID
     * @return 评论数量
     */
    @Override
    public int countCommentsByPostId(Integer postId) {
        return commentMapper.countCommentsByPostId(postId);
    }

    /**
     * 统计用户发表的评论数量
     *
     * @param userId 用户ID
     * @return 评论数量
     */
    @Override
    public int countCommentsByUserId(Long userId) {
        return commentMapper.countCommentsByUserId(userId);
    }
}