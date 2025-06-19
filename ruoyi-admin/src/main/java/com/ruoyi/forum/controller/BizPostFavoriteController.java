package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.forum.domain.BizPostFavorite;
import com.ruoyi.forum.service.IBizPostFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 帖子收藏Controller
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/favorite")
public class BizPostFavoriteController extends BaseController
{
    @Autowired
    private IBizPostFavoriteService bizPostFavoriteService;

    /**
     * 查询帖子收藏列表
     */
    @PreAuthorize("@ss.hasPermi('forum:favorite:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPostFavorite bizPostFavorite)
    {
        startPage();
        List<BizPostFavorite> list = bizPostFavoriteService.selectBizPostFavoriteList(bizPostFavorite);
        return getDataTable(list);
    }

    /**
     * 导出帖子收藏列表
     */
    @PreAuthorize("@ss.hasPermi('forum:favorite:export')")
    @Log(title = "帖子收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPostFavorite bizPostFavorite)
    {
        List<BizPostFavorite> list = bizPostFavoriteService.selectBizPostFavoriteList(bizPostFavorite);
        ExcelUtil<BizPostFavorite> util = new ExcelUtil<BizPostFavorite>(BizPostFavorite.class);
        util.exportExcel(response, list, "帖子收藏数据");
    }

    /**
     * 获取帖子收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:favorite:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(bizPostFavoriteService.selectBizPostFavoriteByUserId(userId));
    }

    /**
     * 新增帖子收藏
     */
    @PreAuthorize("@ss.hasPermi('forum:favorite:add')")
    @Log(title = "帖子收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizPostFavorite bizPostFavorite)
    {
        return toAjax(bizPostFavoriteService.insertBizPostFavorite(bizPostFavorite));
    }

    /**
     * 修改帖子收藏
     */
    @PreAuthorize("@ss.hasPermi('forum:favorite:edit')")
    @Log(title = "帖子收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizPostFavorite bizPostFavorite)
    {
        return toAjax(bizPostFavoriteService.updateBizPostFavorite(bizPostFavorite));
    }

    /**
     * 删除帖子收藏
     */
    @PreAuthorize("@ss.hasPermi('forum:favorite:remove')")
    @Log(title = "帖子收藏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(bizPostFavoriteService.deleteBizPostFavoriteByUserIds(userIds));
    }
}
