package com.ruoyi.user.service;

import com.ruoyi.user.domain.Game;

import java.util.List;

public interface IGameService {

    /**
     * 查询游戏信息
     */
    public Game selectGameById(Integer gameId);

    /**
     * 查询游戏信息列表
     */
    public List<Game> selectGameList(Game game);

    /**
     * 根据游戏类型查询游戏列表
     */
    public List<Game> selectGameListByType(Integer gameTypeId);

    /**
     * 根据游戏名称模糊查询
     */
    public List<Game> selectGameListByName(String gameName);

    /**
     * 查询热门游戏列表
     */
    public List<Game> selectHotGameList(Integer limit);
}