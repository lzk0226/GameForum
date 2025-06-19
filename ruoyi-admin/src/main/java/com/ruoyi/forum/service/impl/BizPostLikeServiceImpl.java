package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizPostLikeMapper;
import com.ruoyi.forum.domain.BizPostLike;
import com.ruoyi.forum.service.IBizPostLikeService;

/**
 * 帖子点赞Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizPostLikeServiceImpl implements IBizPostLikeService 
{
    @Autowired
    private BizPostLikeMapper bizPostLikeMapper;

    /**
     * 查询帖子点赞
     * 
     * @param userId 帖子点赞主键
     * @return 帖子点赞
     */
    @Override
    public BizPostLike selectBizPostLikeByUserId(Long userId)
    {
        return bizPostLikeMapper.selectBizPostLikeByUserId(userId);
    }

    /**
     * 查询帖子点赞列表
     * 
     * @param bizPostLike 帖子点赞
     * @return 帖子点赞
     */
    @Override
    public List<BizPostLike> selectBizPostLikeList(BizPostLike bizPostLike)
    {
        return bizPostLikeMapper.selectBizPostLikeList(bizPostLike);
    }

    /**
     * 新增帖子点赞
     * 
     * @param bizPostLike 帖子点赞
     * @return 结果
     */
    @Override
    public int insertBizPostLike(BizPostLike bizPostLike)
    {
        bizPostLike.setCreateTime(DateUtils.getNowDate());
        return bizPostLikeMapper.insertBizPostLike(bizPostLike);
    }

    /**
     * 修改帖子点赞
     * 
     * @param bizPostLike 帖子点赞
     * @return 结果
     */
    @Override
    public int updateBizPostLike(BizPostLike bizPostLike)
    {
        return bizPostLikeMapper.updateBizPostLike(bizPostLike);
    }

    /**
     * 批量删除帖子点赞
     * 
     * @param userIds 需要删除的帖子点赞主键
     * @return 结果
     */
    @Override
    public int deleteBizPostLikeByUserIds(Long[] userIds)
    {
        return bizPostLikeMapper.deleteBizPostLikeByUserIds(userIds);
    }

    /**
     * 删除帖子点赞信息
     * 
     * @param userId 帖子点赞主键
     * @return 结果
     */
    @Override
    public int deleteBizPostLikeByUserId(Long userId)
    {
        return bizPostLikeMapper.deleteBizPostLikeByUserId(userId);
    }
}
