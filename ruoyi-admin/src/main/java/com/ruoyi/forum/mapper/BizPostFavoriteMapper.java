package com.ruoyi.forum.mapper;

import java.util.List;
import com.ruoyi.forum.domain.BizPostFavorite;

/**
 * 帖子收藏Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface BizPostFavoriteMapper 
{
    /**
     * 查询帖子收藏
     * 
     * @param userId 帖子收藏主键
     * @return 帖子收藏
     */
    public BizPostFavorite selectBizPostFavoriteByUserId(Long userId);

    /**
     * 查询帖子收藏列表
     * 
     * @param bizPostFavorite 帖子收藏
     * @return 帖子收藏集合
     */
    public List<BizPostFavorite> selectBizPostFavoriteList(BizPostFavorite bizPostFavorite);

    /**
     * 新增帖子收藏
     * 
     * @param bizPostFavorite 帖子收藏
     * @return 结果
     */
    public int insertBizPostFavorite(BizPostFavorite bizPostFavorite);

    /**
     * 修改帖子收藏
     * 
     * @param bizPostFavorite 帖子收藏
     * @return 结果
     */
    public int updateBizPostFavorite(BizPostFavorite bizPostFavorite);

    /**
     * 删除帖子收藏
     * 
     * @param userId 帖子收藏主键
     * @return 结果
     */
    public int deleteBizPostFavoriteByUserId(Long userId);

    /**
     * 批量删除帖子收藏
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizPostFavoriteByUserIds(Long[] userIds);
}
