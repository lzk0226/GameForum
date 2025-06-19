package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.forum.domain.BizSection;
import com.ruoyi.forum.service.IBizSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 论坛版块Controller
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/section")
public class BizSectionController extends BaseController
{
    @Autowired
    private IBizSectionService bizSectionService;

    /**
     * 查询论坛版块列表
     */
    @PreAuthorize("@ss.hasPermi('forum:section:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizSection bizSection)
    {
        startPage();
        List<BizSection> list = bizSectionService.selectBizSectionList(bizSection);
        return getDataTable(list);
    }

    /**
     * 导出论坛版块列表
     */
    @PreAuthorize("@ss.hasPermi('forum:section:export')")
    @Log(title = "论坛版块", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizSection bizSection)
    {
        List<BizSection> list = bizSectionService.selectBizSectionList(bizSection);
        ExcelUtil<BizSection> util = new ExcelUtil<BizSection>(BizSection.class);
        util.exportExcel(response, list, "论坛版块数据");
    }

    /**
     * 获取论坛版块详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:section:query')")
    @GetMapping(value = "/{sectionId}")
    public AjaxResult getInfo(@PathVariable("sectionId") Long sectionId)
    {
        return success(bizSectionService.selectBizSectionBySectionId(sectionId));
    }

    /**
     * 新增论坛版块
     */
    @PreAuthorize("@ss.hasPermi('forum:section:add')")
    @Log(title = "论坛版块", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizSection bizSection)
    {
        return toAjax(bizSectionService.insertBizSection(bizSection));
    }

    /**
     * 修改论坛版块
     */
    @PreAuthorize("@ss.hasPermi('forum:section:edit')")
    @Log(title = "论坛版块", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizSection bizSection)
    {
        return toAjax(bizSectionService.updateBizSection(bizSection));
    }

    /**
     * 删除论坛版块
     */
    @PreAuthorize("@ss.hasPermi('forum:section:remove')")
    @Log(title = "论坛版块", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sectionIds}")
    public AjaxResult remove(@PathVariable Long[] sectionIds)
    {
        return toAjax(bizSectionService.deleteBizSectionBySectionIds(sectionIds));
    }
}
