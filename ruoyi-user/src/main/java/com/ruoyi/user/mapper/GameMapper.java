package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.Game;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameMapper {

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
    public List<Game> selectGameListByType(@Param("gameTypeId") Integer gameTypeId);

    /**
     * 根据游戏名称模糊查询
     */
    public List<Game> selectGameListByName(@Param("gameName") String gameName);

    /**
     * 查询热门游戏列表 (按创建时间倒序)
     */
    public List<Game> selectHotGameList(@Param("limit") Integer limit);
}
