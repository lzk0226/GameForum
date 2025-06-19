package com.ruoyi.forum.mapper;

import com.ruoyi.forum.domain.BizComment;

import java.util.List;

/**
 * 评论Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public interface BizCommentMapper 
{
    /**
     * 查询评论
     * 
     * @param commentId 评论主键
     * @return 评论
     */
    public BizComment selectBizCommentByCommentId(Long commentId);

    /**
     * 查询评论列表
     * 
     * @param bizComment 评论
     * @return 评论集合
     */
    public List<BizComment> selectBizCommentList(BizComment bizComment);

    /**
     * 新增评论
     * 
     * @param bizComment 评论
     * @return 结果
     */
    public int insertBizComment(BizComment bizComment);

    /**
     * 修改评论
     * 
     * @param bizComment 评论
     * @return 结果
     */
    public int updateBizComment(BizComment bizComment);

    /**
     * 删除评论
     * 
     * @param commentId 评论主键
     * @return 结果
     */
    public int deleteBizCommentByCommentId(Long commentId);

    /**
     * 批量删除评论
     * 
     * @param commentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCommentByCommentIds(Long[] commentIds);
}
