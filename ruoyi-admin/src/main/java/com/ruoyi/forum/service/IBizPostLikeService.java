package com.ruoyi.forum.service;

import java.util.List;
import com.ruoyi.forum.domain.BizPostLike;

/**
 * 帖子点赞Service接口
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public interface IBizPostLikeService 
{
    /**
     * 查询帖子点赞
     * 
     * @param userId 帖子点赞主键
     * @return 帖子点赞
     */
    public BizPostLike selectBizPostLikeByUserId(Long userId);

    /**
     * 查询帖子点赞列表
     * 
     * @param bizPostLike 帖子点赞
     * @return 帖子点赞集合
     */
    public List<BizPostLike> selectBizPostLikeList(BizPostLike bizPostLike);

    /**
     * 新增帖子点赞
     * 
     * @param bizPostLike 帖子点赞
     * @return 结果
     */
    public int insertBizPostLike(BizPostLike bizPostLike);

    /**
     * 修改帖子点赞
     * 
     * @param bizPostLike 帖子点赞
     * @return 结果
     */
    public int updateBizPostLike(BizPostLike bizPostLike);

    /**
     * 批量删除帖子点赞
     * 
     * @param userIds 需要删除的帖子点赞主键集合
     * @return 结果
     */
    public int deleteBizPostLikeByUserIds(Long[] userIds);

    /**
     * 删除帖子点赞信息
     * 
     * @param userId 帖子点赞主键
     * @return 结果
     */
    public int deleteBizPostLikeByUserId(Long userId);
}
