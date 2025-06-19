package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizCommentMapper;
import com.ruoyi.forum.domain.BizComment;
import com.ruoyi.forum.service.IBizCommentService;

/**
 * 评论Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizCommentServiceImpl implements IBizCommentService 
{
    @Autowired
    private BizCommentMapper bizCommentMapper;

    /**
     * 查询评论
     * 
     * @param commentId 评论主键
     * @return 评论
     */
    @Override
    public BizComment selectBizCommentByCommentId(Long commentId)
    {
        return bizCommentMapper.selectBizCommentByCommentId(commentId);
    }

    /**
     * 查询评论列表
     * 
     * @param bizComment 评论
     * @return 评论
     */
    @Override
    public List<BizComment> selectBizCommentList(BizComment bizComment)
    {
        return bizCommentMapper.selectBizCommentList(bizComment);
    }

    /**
     * 新增评论
     * 
     * @param bizComment 评论
     * @return 结果
     */
    @Override
    public int insertBizComment(BizComment bizComment)
    {
        bizComment.setCreateTime(DateUtils.getNowDate());
        return bizCommentMapper.insertBizComment(bizComment);
    }

    /**
     * 修改评论
     * 
     * @param bizComment 评论
     * @return 结果
     */
    @Override
    public int updateBizComment(BizComment bizComment)
    {
        bizComment.setUpdateTime(DateUtils.getNowDate());
        return bizCommentMapper.updateBizComment(bizComment);
    }

    /**
     * 批量删除评论
     * 
     * @param commentIds 需要删除的评论主键
     * @return 结果
     */
    @Override
    public int deleteBizCommentByCommentIds(Long[] commentIds)
    {
        return bizCommentMapper.deleteBizCommentByCommentIds(commentIds);
    }

    /**
     * 删除评论信息
     * 
     * @param commentId 评论主键
     * @return 结果
     */
    @Override
    public int deleteBizCommentByCommentId(Long commentId)
    {
        return bizCommentMapper.deleteBizCommentByCommentId(commentId);
    }
}
