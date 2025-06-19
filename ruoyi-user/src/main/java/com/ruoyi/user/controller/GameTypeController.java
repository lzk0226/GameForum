package com.ruoyi.user.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.user.domain.GameType;
import com.ruoyi.user.service.IGameTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明: 游戏类型控制器
 * 文件创建时间:2025/5/24下午 6:24
 * @Author : SockLightDust
 */
@RestController
@RequestMapping("/user/gameType")
public class GameTypeController {

    @Autowired
    private IGameTypeService gameTypeService;

    /**
     * 获取游戏类型信息详细信息
     */
    @GetMapping(value = "/{typeId}")
    public R<GameType> getInfo(@PathVariable("typeId") Integer typeId) {
        GameType gameType = gameTypeService.selectGameTypeById(typeId);
        return gameType != null ? R.ok(gameType) : R.fail("游戏类型不存在");
    }

    /**
     * 查询游戏类型信息列表
     */
    @GetMapping("/list")
    public R<List<GameType>> list(GameType gameType) {
        List<GameType> list = gameTypeService.selectGameTypeList(gameType);
        return R.ok(list);
    }

    /**
     * 查询所有游戏类型列表
     */
    @GetMapping("/all")
    public R<List<GameType>> allList() {
        List<GameType> list = gameTypeService.selectAllGameTypeList();
        return R.ok(list);
    }

    /**
     * 根据类型名称搜索游戏类型
     */
    @GetMapping("/search")
    public R<List<GameType>> searchByName(@RequestParam("name") String typeName) {
        List<GameType> list = gameTypeService.selectGameTypeListByName(typeName);
        return R.ok(list);
    }
}