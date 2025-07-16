package com.ruoyi.user.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.user.domain.Section;
import com.ruoyi.user.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明: 论坛版块控制器
 * 文件创建时间:2025/5/24下午 7:05
 * @Author : SockLightDust
 */
@RestController
@RequestMapping("/user/section")
public class SectionController {

    @Autowired
    private ISectionService sectionService;

    /**
     * 获取论坛版块信息详细信息
     */
    @GetMapping(value = "/{sectionId}")
    public R<Section> getInfo(@PathVariable("sectionId") Integer sectionId) {
        Section section = sectionService.selectSectionById(sectionId);
        return section != null ? R.ok(section) : R.fail("论坛版块不存在");
    }

    /**
     * 查询论坛版块信息列表
     */
    @GetMapping("/list")
    public R<List<Section>> list(Section section) {
        List<Section> list = sectionService.selectSectionList(section);
        return R.ok(list);
    }

    /**
     * 根据游戏ID查询版块列表
     */
    @GetMapping("/game/{gameId}")
    public R<List<Section>> listByGameId(@PathVariable("gameId") Integer gameId) {
        List<Section> list = sectionService.selectSectionListByGameId(gameId);
        return R.ok(list);
    }

    /**
     * 根据版块名称搜索版块
     */
    @GetMapping("/search")
    public R<List<Section>> searchByName(@RequestParam("name") String sectionName) {
        List<Section> list = sectionService.selectSectionListByName(sectionName);
        return R.ok(list);
    }

    /**
     * 查询热门版块列表
     */
    @GetMapping("/hot")
    public R<List<Section>> hotList(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Section> list = sectionService.selectHotSectionList(limit);
        return R.ok(list);
    }

    /**
     * 查询所有版块列表
     */
    @GetMapping("/all")
    public R<List<Section>> allList() {
        List<Section> list = sectionService.selectAllSectionList();
        return R.ok(list);
    }
    /**
     * 根据板块ID查询对应的游戏ID
     */
    @GetMapping("/gameId/{sectionId}")
    public R<Integer> getGameIdBySectionId(@PathVariable("sectionId") Integer sectionId) {
        Integer gameId = sectionService.selectGameIdBySectionId(sectionId);
        return gameId != null ? R.ok(gameId) : R.fail("版块不存在或未关联游戏");
    }
}