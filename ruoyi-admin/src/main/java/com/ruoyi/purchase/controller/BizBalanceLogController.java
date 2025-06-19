package com.ruoyi.purchase.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.purchase.domain.BizBalanceLog;
import com.ruoyi.purchase.service.IBizBalanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 余额变动日志Controller
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/purchase/log")
public class BizBalanceLogController extends BaseController
{
    @Autowired
    private IBizBalanceLogService bizBalanceLogService;

    /**
     * 查询余额变动日志列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizBalanceLog bizBalanceLog)
    {
        startPage();
        List<BizBalanceLog> list = bizBalanceLogService.selectBizBalanceLogList(bizBalanceLog);
        return getDataTable(list);
    }

    /**
     * 导出余额变动日志列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:log:export')")
    @Log(title = "余额变动日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizBalanceLog bizBalanceLog)
    {
        List<BizBalanceLog> list = bizBalanceLogService.selectBizBalanceLogList(bizBalanceLog);
        ExcelUtil<BizBalanceLog> util = new ExcelUtil<BizBalanceLog>(BizBalanceLog.class);
        util.exportExcel(response, list, "余额变动日志数据");
    }

    /**
     * 获取余额变动日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:log:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return success(bizBalanceLogService.selectBizBalanceLogByLogId(logId));
    }

    /**
     * 新增余额变动日志
     */
    @PreAuthorize("@ss.hasPermi('purchase:log:add')")
    @Log(title = "余额变动日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizBalanceLog bizBalanceLog)
    {
        return toAjax(bizBalanceLogService.insertBizBalanceLog(bizBalanceLog));
    }

    /**
     * 修改余额变动日志
     */
    @PreAuthorize("@ss.hasPermi('purchase:log:edit')")
    @Log(title = "余额变动日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizBalanceLog bizBalanceLog)
    {
        return toAjax(bizBalanceLogService.updateBizBalanceLog(bizBalanceLog));
    }

    /**
     * 删除余额变动日志
     */
    @PreAuthorize("@ss.hasPermi('purchase:log:remove')")
    @Log(title = "余额变动日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(bizBalanceLogService.deleteBizBalanceLogByLogIds(logIds));
    }
}
