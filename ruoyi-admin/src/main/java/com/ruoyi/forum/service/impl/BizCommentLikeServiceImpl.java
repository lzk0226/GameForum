package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizCommentLikeMapper;
import com.ruoyi.forum.domain.BizCommentLike;
import com.ruoyi.forum.service.IBizCommentLikeService;

/**
 * 评论点赞Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizCommentLikeServiceImpl implements IBizCommentLikeService 
{
    @Autowired
    private BizCommentLikeMapper bizCommentLikeMapper;

    /**
     * 查询评论点赞
     * 
     * @param userId 评论点赞主键
     * @return 评论点赞
     */
    @Override
    public BizCommentLike selectBizCommentLikeByUserId(Long userId)
    {
        return bizCommentLikeMapper.selectBizCommentLikeByUserId(userId);
    }

    /**
     * 查询评论点赞列表
     * 
     * @param bizCommentLike 评论点赞
     * @return 评论点赞
     */
    @Override
    public List<BizCommentLike> selectBizCommentLikeList(BizCommentLike bizCommentLike)
    {
        return bizCommentLikeMapper.selectBizCommentLikeList(bizCommentLike);
    }

    /**
     * 新增评论点赞
     * 
     * @param bizCommentLike 评论点赞
     * @return 结果
     */
    @Override
    public int insertBizCommentLike(BizCommentLike bizCommentLike)
    {
        bizCommentLike.setCreateTime(DateUtils.getNowDate());
        return bizCommentLikeMapper.insertBizCommentLike(bizCommentLike);
    }

    /**
     * 修改评论点赞
     * 
     * @param bizCommentLike 评论点赞
     * @return 结果
     */
    @Override
    public int updateBizCommentLike(BizCommentLike bizCommentLike)
    {
        return bizCommentLikeMapper.updateBizCommentLike(bizCommentLike);
    }

    /**
     * 批量删除评论点赞
     * 
     * @param userIds 需要删除的评论点赞主键
     * @return 结果
     */
    @Override
    public int deleteBizCommentLikeByUserIds(Long[] userIds)
    {
        return bizCommentLikeMapper.deleteBizCommentLikeByUserIds(userIds);
    }

    /**
     * 删除评论点赞信息
     * 
     * @param userId 评论点赞主键
     * @return 结果
     */
    @Override
    public int deleteBizCommentLikeByUserId(Long userId)
    {
        return bizCommentLikeMapper.deleteBizCommentLikeByUserId(userId);
    }
}
