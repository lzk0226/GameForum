package com.ruoyi.user.service.impl;

import com.ruoyi.user.mapper.GameMapper;
import com.ruoyi.user.domain.Game;
import com.ruoyi.user.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/24下午 6:12
 * @Author : SockLightDust
 */
@Service
public class GameServiceImpl implements IGameService {

    @Autowired
    private GameMapper gameMapper;

    /**
     * 查询游戏信息
     */
    @Override
    public Game selectGameById(Integer gameId) {
        return gameMapper.selectGameById(gameId);
    }

    /**
     * 查询游戏信息列表
     */
    @Override
    public List<Game> selectGameList(Game game) {
        return gameMapper.selectGameList(game);
    }

    /**
     * 根据游戏类型查询游戏列表
     */
    @Override
    public List<Game> selectGameListByType(Integer gameTypeId) {
        return gameMapper.selectGameListByType(gameTypeId);
    }

    /**
     * 根据游戏名称模糊查询
     */
    @Override
    public List<Game> selectGameListByName(String gameName) {
        return gameMapper.selectGameListByName(gameName);
    }

    /**
     * 查询热门游戏列表
     */
    @Override
    public List<Game> selectHotGameList(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认查询10条
        }
        return gameMapper.selectHotGameList(limit);
    }
}