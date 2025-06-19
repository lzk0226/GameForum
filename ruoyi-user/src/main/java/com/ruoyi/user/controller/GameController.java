package com.ruoyi.user.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.user.domain.Game;
import com.ruoyi.user.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明: 游戏控制器
 * 文件创建时间:2025/5/24下午 6:12
 * @Author : SockLightDust
 */
@RestController
@RequestMapping("/user/game")
public class GameController {

    @Autowired
    private IGameService gameService;

    /**
     * 获取游戏信息详细信息
     */
    @GetMapping(value = "/{gameId}")
    public R<Game> getInfo(@PathVariable("gameId") Integer gameId) {
        Game game = gameService.selectGameById(gameId);
        return game != null ? R.ok(game) : R.fail("游戏不存在");
    }

    /**
     * 查询游戏信息列表
     */
    @GetMapping("/list")
    public R<List<Game>> list(Game game) {
        List<Game> list = gameService.selectGameList(game);
        return R.ok(list);
    }

    /**
     * 根据游戏类型查询游戏列表
     */
    @GetMapping("/type/{gameTypeId}")
    public R<List<Game>> listByType(@PathVariable("gameTypeId") Integer gameTypeId) {
        List<Game> list = gameService.selectGameListByType(gameTypeId);
        return R.ok(list);
    }

    /**
     * 根据游戏名称搜索游戏
     */
    @GetMapping("/search")
    public R<List<Game>> searchByName(@RequestParam("name") String gameName) {
        List<Game> list = gameService.selectGameListByName(gameName);
        return R.ok(list);
    }

    /**
     * 查询热门游戏列表
     */
    @GetMapping("/hot")
    public R<List<Game>> hotList(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Game> list = gameService.selectHotGameList(limit);
        return R.ok(list);
    }
}