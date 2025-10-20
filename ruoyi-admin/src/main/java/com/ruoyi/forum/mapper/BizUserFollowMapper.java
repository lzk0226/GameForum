package com.ruoyi.forum.mapper;

import java.util.List;
import com.ruoyi.forum.domain.BizUserFollow;

/**
 * 用户关注Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface BizUserFollowMapper 
{
    /**
     * 查询用户关注
     * 
     * @param followerId 用户关注主键
     * @return 用户关注
     */
    public BizUserFollow selectBizUserFollowByFollowerId(Long followerId);

    /**
     * 查询用户关注列表
     * 
     * @param bizUserFollow 用户关注
     * @return 用户关注集合
     */
    public List<BizUserFollow> selectBizUserFollowList(BizUserFollow bizUserFollow);

    /**
     * 新增用户关注
     * 
     * @param bizUserFollow 用户关注
     * @return 结果
     */
    public int insertBizUserFollow(BizUserFollow bizUserFollow);

    /**
     * 修改用户关注
     * 
     * @param bizUserFollow 用户关注
     * @return 结果
     */
    public int updateBizUserFollow(BizUserFollow bizUserFollow);

    /**
     * 删除用户关注
     * 
     * @param followerId 用户关注主键
     * @return 结果
     */
    public int deleteBizUserFollowByFollowerId(Long followerId);

    /**
     * 批量删除用户关注
     * 
     * @param followerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizUserFollowByFollowerIds(Long[] followerIds);
}
