package com.ruoyi.user.service;

import com.ruoyi.user.domain.GameType;

import java.util.List;

public interface IGameTypeService {

    /**
     * 查询游戏类型信息
     */
    public GameType selectGameTypeById(Integer typeId);

    /**
     * 查询游戏类型信息列表
     */
    public List<GameType> selectGameTypeList(GameType gameType);

    /**
     * 查询所有游戏类型列表
     */
    public List<GameType> selectAllGameTypeList();

    /**
     * 根据类型名称模糊查询
     */
    public List<GameType> selectGameTypeListByName(String typeName);
}