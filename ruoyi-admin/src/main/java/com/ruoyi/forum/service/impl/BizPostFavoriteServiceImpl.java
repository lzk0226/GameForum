package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizPostFavoriteMapper;
import com.ruoyi.forum.domain.BizPostFavorite;
import com.ruoyi.forum.service.IBizPostFavoriteService;

/**
 * 帖子收藏Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizPostFavoriteServiceImpl implements IBizPostFavoriteService 
{
    @Autowired
    private BizPostFavoriteMapper bizPostFavoriteMapper;

    /**
     * 查询帖子收藏
     * 
     * @param userId 帖子收藏主键
     * @return 帖子收藏
     */
    @Override
    public BizPostFavorite selectBizPostFavoriteByUserId(Long userId)
    {
        return bizPostFavoriteMapper.selectBizPostFavoriteByUserId(userId);
    }

    /**
     * 查询帖子收藏列表
     * 
     * @param bizPostFavorite 帖子收藏
     * @return 帖子收藏
     */
    @Override
    public List<BizPostFavorite> selectBizPostFavoriteList(BizPostFavorite bizPostFavorite)
    {
        return bizPostFavoriteMapper.selectBizPostFavoriteList(bizPostFavorite);
    }

    /**
     * 新增帖子收藏
     * 
     * @param bizPostFavorite 帖子收藏
     * @return 结果
     */
    @Override
    public int insertBizPostFavorite(BizPostFavorite bizPostFavorite)
    {
        bizPostFavorite.setCreateTime(DateUtils.getNowDate());
        return bizPostFavoriteMapper.insertBizPostFavorite(bizPostFavorite);
    }

    /**
     * 修改帖子收藏
     * 
     * @param bizPostFavorite 帖子收藏
     * @return 结果
     */
    @Override
    public int updateBizPostFavorite(BizPostFavorite bizPostFavorite)
    {
        return bizPostFavoriteMapper.updateBizPostFavorite(bizPostFavorite);
    }

    /**
     * 批量删除帖子收藏
     * 
     * @param userIds 需要删除的帖子收藏主键
     * @return 结果
     */
    @Override
    public int deleteBizPostFavoriteByUserIds(Long[] userIds)
    {
        return bizPostFavoriteMapper.deleteBizPostFavoriteByUserIds(userIds);
    }

    /**
     * 删除帖子收藏信息
     * 
     * @param userId 帖子收藏主键
     * @return 结果
     */
    @Override
    public int deleteBizPostFavoriteByUserId(Long userId)
    {
        return bizPostFavoriteMapper.deleteBizPostFavoriteByUserId(userId);
    }
}
