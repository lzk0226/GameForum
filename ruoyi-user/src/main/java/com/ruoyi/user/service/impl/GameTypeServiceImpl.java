package com.ruoyi.user.service.impl;

import com.ruoyi.user.domain.GameType;
import com.ruoyi.user.mapper.GameTypeMapper;
import com.ruoyi.user.service.IGameTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/24下午 6:24
 * @Author : SockLightDust
 */
@Service
public class GameTypeServiceImpl implements IGameTypeService {

    @Autowired
    private GameTypeMapper gameTypeMapper;

    /**
     * 查询游戏类型信息
     */
    @Override
    public GameType selectGameTypeById(Integer typeId) {
        return gameTypeMapper.selectGameTypeById(typeId);
    }

    /**
     * 查询游戏类型信息列表
     */
    @Override
    public List<GameType> selectGameTypeList(GameType gameType) {
        return gameTypeMapper.selectGameTypeList(gameType);
    }

    /**
     * 查询所有游戏类型列表
     */
    @Override
    public List<GameType> selectAllGameTypeList() {
        return gameTypeMapper.selectAllGameTypeList();
    }

    /**
     * 根据类型名称模糊查询
     */
    @Override
    public List<GameType> selectGameTypeListByName(String typeName) {
        return gameTypeMapper.selectGameTypeListByName(typeName);
    }
}
