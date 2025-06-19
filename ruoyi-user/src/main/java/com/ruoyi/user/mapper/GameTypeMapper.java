package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.GameType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameTypeMapper {

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
    public List<GameType> selectGameTypeListByName(@Param("typeName") String typeName);
}