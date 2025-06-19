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
import com.ruoyi.forum.domain.BizAnnouncement;
import com.ruoyi.forum.service.IBizAnnouncementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公告栏Controller
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/forum/announcement")
public class BizAnnouncementController extends BaseController
{
    @Autowired
    private IBizAnnouncementService bizAnnouncementService;

    /**
     * 查询公告栏列表
     */
    @PreAuthorize("@ss.hasPermi('forum:announcement:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizAnnouncement bizAnnouncement)
    {
        startPage();
        List<BizAnnouncement> list = bizAnnouncementService.selectBizAnnouncementList(bizAnnouncement);
        return getDataTable(list);
    }

    /**
     * 导出公告栏列表
     */
    @PreAuthorize("@ss.hasPermi('forum:announcement:export')")
    @Log(title = "公告栏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizAnnouncement bizAnnouncement)
    {
        List<BizAnnouncement> list = bizAnnouncementService.selectBizAnnouncementList(bizAnnouncement);
        ExcelUtil<BizAnnouncement> util = new ExcelUtil<BizAnnouncement>(BizAnnouncement.class);
        util.exportExcel(response, list, "公告栏数据");
    }

    /**
     * 获取公告栏详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:announcement:query')")
    @GetMapping(value = "/{titel}")
    public AjaxResult getInfo(@PathVariable("titel") String titel)
    {
        return success(bizAnnouncementService.selectBizAnnouncementByTitel(titel));
    }

    /**
     * 新增公告栏
     */
    @PreAuthorize("@ss.hasPermi('forum:announcement:add')")
    @Log(title = "公告栏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizAnnouncement bizAnnouncement)
    {
        return toAjax(bizAnnouncementService.insertBizAnnouncement(bizAnnouncement));
    }

    /**
     * 修改公告栏
     */
    @PreAuthorize("@ss.hasPermi('forum:announcement:edit')")
    @Log(title = "公告栏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizAnnouncement bizAnnouncement)
    {
        return toAjax(bizAnnouncementService.updateBizAnnouncement(bizAnnouncement));
    }

    /**
     * 删除公告栏
     */
    @PreAuthorize("@ss.hasPermi('forum:announcement:remove')")
    @Log(title = "公告栏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{titels}")
    public AjaxResult remove(@PathVariable String[] titels)
    {
        return toAjax(bizAnnouncementService.deleteBizAnnouncementByTitels(titels));
    }
}
