package com.ruoyi.forum.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.forum.domain.BizGameType;
import com.ruoyi.forum.service.IBizGameTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 游戏类型Controller
 *
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/type")
public class BizGameTypeController extends BaseController
{
    @Autowired
    private IBizGameTypeService bizGameTypeService;

    /**
     * 查询游戏类型列表
     */
    @PreAuthorize("@ss.hasPermi('forum:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizGameType bizGameType)
    {
        startPage();
        List<BizGameType> list = bizGameTypeService.selectBizGameTypeList(bizGameType);
        return getDataTable(list);
    }

    /**
     * 导出游戏类型列表
     */
    @PreAuthorize("@ss.hasPermi('forum:type:export')")
    @Log(title = "游戏类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizGameType bizGameType)
    {
        List<BizGameType> list = bizGameTypeService.selectBizGameTypeList(bizGameType);
        ExcelUtil<BizGameType> util = new ExcelUtil<BizGameType>(BizGameType.class);
        util.exportExcel(response, list, "游戏类型数据");
    }

    /**
     * 获取游戏类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:type:query')")
    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") Long typeId)
    {
        return success(bizGameTypeService.selectBizGameTypeByTypeId(typeId));
    }

    /**
     * 新增游戏类型
     */
    @PreAuthorize("@ss.hasPermi('forum:type:add')")
    @Log(title = "游戏类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizGameType bizGameType)
    {
        return toAjax(bizGameTypeService.insertBizGameType(bizGameType));
    }

    /**
     * 修改游戏类型
     */
    @PreAuthorize("@ss.hasPermi('forum:type:edit')")
    @Log(title = "游戏类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizGameType bizGameType)
    {
        return toAjax(bizGameTypeService.updateBizGameType(bizGameType));
    }

    /**
     * 删除游戏类型
     */
    @PreAuthorize("@ss.hasPermi('forum:type:remove')")
    @Log(title = "游戏类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{typeIds}")
    public AjaxResult remove(@PathVariable Long[] typeIds)
    {
        return toAjax(bizGameTypeService.deleteBizGameTypeByTypeIds(typeIds));
    }
}
