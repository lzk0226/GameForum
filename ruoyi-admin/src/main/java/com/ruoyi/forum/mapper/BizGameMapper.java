package com.ruoyi.forum.mapper;

import com.ruoyi.forum.domain.BizGame;

import java.util.List;

/**
 * 游戏Mapper接口
 *
 * @author ruoyi
 * @date 2025-05-17
 */
public interface BizGameMapper
{
    /**
     * 查询游戏
     *
     * @param gameId 游戏主键
     * @return 游戏
     */
    public BizGame selectBizGameByGameId(Long gameId);

    /**
     * 查询游戏列表
     *
     * @param bizGame 游戏
     * @return 游戏集合
     */
    public List<BizGame> selectBizGameList(BizGame bizGame);

    /**
     * 新增游戏
     *
     * @param bizGame 游戏
     * @return 结果
     */
    public int insertBizGame(BizGame bizGame);

    /**
     * 修改游戏
     *
     * @param bizGame 游戏
     * @return 结果
     */
    public int updateBizGame(BizGame bizGame);

    /**
     * 删除游戏
     *
     * @param gameId 游戏主键
     * @return 结果
     */
    public int deleteBizGameByGameId(Long gameId);

    /**
     * 批量删除游戏
     *
     * @param gameIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizGameByGameIds(Long[] gameIds);
}
