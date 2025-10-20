package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizUserFollowMapper;
import com.ruoyi.forum.domain.BizUserFollow;
import com.ruoyi.forum.service.IBizUserFollowService;

/**
 * 用户关注Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class BizUserFollowServiceImpl implements IBizUserFollowService 
{
    @Autowired
    private BizUserFollowMapper bizUserFollowMapper;

    /**
     * 查询用户关注
     * 
     * @param followerId 用户关注主键
     * @return 用户关注
     */
    @Override
    public BizUserFollow selectBizUserFollowByFollowerId(Long followerId)
    {
        return bizUserFollowMapper.selectBizUserFollowByFollowerId(followerId);
    }

    /**
     * 查询用户关注列表
     * 
     * @param bizUserFollow 用户关注
     * @return 用户关注
     */
    @Override
    public List<BizUserFollow> selectBizUserFollowList(BizUserFollow bizUserFollow)
    {
        return bizUserFollowMapper.selectBizUserFollowList(bizUserFollow);
    }

    /**
     * 新增用户关注
     * 
     * @param bizUserFollow 用户关注
     * @return 结果
     */
    @Override
    public int insertBizUserFollow(BizUserFollow bizUserFollow)
    {
        bizUserFollow.setCreateTime(DateUtils.getNowDate());
        return bizUserFollowMapper.insertBizUserFollow(bizUserFollow);
    }

    /**
     * 修改用户关注
     * 
     * @param bizUserFollow 用户关注
     * @return 结果
     */
    @Override
    public int updateBizUserFollow(BizUserFollow bizUserFollow)
    {
        return bizUserFollowMapper.updateBizUserFollow(bizUserFollow);
    }

    /**
     * 批量删除用户关注
     * 
     * @param followerIds 需要删除的用户关注主键
     * @return 结果
     */
    @Override
    public int deleteBizUserFollowByFollowerIds(Long[] followerIds)
    {
        return bizUserFollowMapper.deleteBizUserFollowByFollowerIds(followerIds);
    }

    /**
     * 删除用户关注信息
     * 
     * @param followerId 用户关注主键
     * @return 结果
     */
    @Override
    public int deleteBizUserFollowByFollowerId(Long followerId)
    {
        return bizUserFollowMapper.deleteBizUserFollowByFollowerId(followerId);
    }
}
