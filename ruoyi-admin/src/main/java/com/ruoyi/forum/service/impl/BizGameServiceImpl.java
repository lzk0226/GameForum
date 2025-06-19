package com.ruoyi.forum.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.forum.domain.BizGame;
import com.ruoyi.forum.mapper.BizGameMapper;
import com.ruoyi.forum.service.IBizGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏Service业务层处理
 *
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizGameServiceImpl implements IBizGameService
{
    @Autowired
    private BizGameMapper bizGameMapper;

    /**
     * 查询游戏
     *
     * @param gameId 游戏主键
     * @return 游戏
     */
    @Override
    public BizGame selectBizGameByGameId(Long gameId)
    {
        return bizGameMapper.selectBizGameByGameId(gameId);
    }

    /**
     * 查询游戏列表
     *
     * @param bizGame 游戏
     * @return 游戏
     */
    @Override
    public List<BizGame> selectBizGameList(BizGame bizGame)
    {
        return bizGameMapper.selectBizGameList(bizGame);
    }

    /**
     * 新增游戏
     *
     * @param bizGame 游戏
     * @return 结果
     */
    @Override
    public int insertBizGame(BizGame bizGame)
    {
        bizGame.setCreateTime(DateUtils.getNowDate());
        return bizGameMapper.insertBizGame(bizGame);
    }

    /**
     * 修改游戏
     *
     * @param bizGame 游戏
     * @return 结果
     */
    @Override
    public int updateBizGame(BizGame bizGame)
    {
        bizGame.setUpdateTime(DateUtils.getNowDate());
        return bizGameMapper.updateBizGame(bizGame);
    }

    /**
     * 批量删除游戏
     *
     * @param gameIds 需要删除的游戏主键
     * @return 结果
     */
    @Override
    public int deleteBizGameByGameIds(Long[] gameIds)
    {
        return bizGameMapper.deleteBizGameByGameIds(gameIds);
    }

    /**
     * 删除游戏信息
     *
     * @param gameId 游戏主键
     * @return 结果
     */
    @Override
    public int deleteBizGameByGameId(Long gameId)
    {
        return bizGameMapper.deleteBizGameByGameId(gameId);
    }
}
