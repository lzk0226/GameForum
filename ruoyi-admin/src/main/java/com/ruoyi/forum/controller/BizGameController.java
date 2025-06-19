package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.forum.domain.BizGame;
import com.ruoyi.forum.service.IBizGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 游戏Controller
 *
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/game")
public class BizGameController extends BaseController
{
    @Autowired
    private IBizGameService bizGameService;

    /**
     * 查询游戏列表
     */
    @PreAuthorize("@ss.hasPermi('forum:game:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizGame bizGame)
    {
        startPage();
        List<BizGame> list = bizGameService.selectBizGameList(bizGame);
        return getDataTable(list);
    }

    /**
     * 导出游戏列表
     */
    @PreAuthorize("@ss.hasPermi('forum:game:export')")
    @Log(title = "游戏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizGame bizGame)
    {
        List<BizGame> list = bizGameService.selectBizGameList(bizGame);
        ExcelUtil<BizGame> util = new ExcelUtil<BizGame>(BizGame.class);
        util.exportExcel(response, list, "游戏数据");
    }

    /**
     * 获取游戏详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:game:query')")
    @GetMapping(value = "/{gameId}")
    public AjaxResult getInfo(@PathVariable("gameId") Long gameId)
    {
        return success(bizGameService.selectBizGameByGameId(gameId));
    }

    /**
     * 新增游戏
     */
    @PreAuthorize("@ss.hasPermi('forum:game:add')")
    @Log(title = "游戏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizGame bizGame)
    {
        return toAjax(bizGameService.insertBizGame(bizGame));
    }

    /**
     * 修改游戏
     */
    @PreAuthorize("@ss.hasPermi('forum:game:edit')")
    @Log(title = "游戏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizGame bizGame)
    {
        return toAjax(bizGameService.updateBizGame(bizGame));
    }

    /**
     * 删除游戏
     */
    @PreAuthorize("@ss.hasPermi('forum:game:remove')")
    @Log(title = "游戏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gameIds}")
    public AjaxResult remove(@PathVariable Long[] gameIds)
    {
        return toAjax(bizGameService.deleteBizGameByGameIds(gameIds));
    }
}
