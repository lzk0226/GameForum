package com.ruoyi.purchase.controller;

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
import com.ruoyi.purchase.domain.BizCdkStock;
import com.ruoyi.purchase.service.IBizCdkStockService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * CDK库存Controller
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/purchase/stock")
public class BizCdkStockController extends BaseController
{
    @Autowired
    private IBizCdkStockService bizCdkStockService;

    /**
     * 查询CDK库存列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:stock:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCdkStock bizCdkStock)
    {
        startPage();
        List<BizCdkStock> list = bizCdkStockService.selectBizCdkStockList(bizCdkStock);
        return getDataTable(list);
    }

    /**
     * 导出CDK库存列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:stock:export')")
    @Log(title = "CDK库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCdkStock bizCdkStock)
    {
        List<BizCdkStock> list = bizCdkStockService.selectBizCdkStockList(bizCdkStock);
        ExcelUtil<BizCdkStock> util = new ExcelUtil<BizCdkStock>(BizCdkStock.class);
        util.exportExcel(response, list, "CDK库存数据");
    }

    /**
     * 获取CDK库存详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:stock:query')")
    @GetMapping(value = "/{stockId}")
    public AjaxResult getInfo(@PathVariable("stockId") Long stockId)
    {
        return success(bizCdkStockService.selectBizCdkStockByStockId(stockId));
    }

    /**
     * 新增CDK库存
     */
    @PreAuthorize("@ss.hasPermi('purchase:stock:add')")
    @Log(title = "CDK库存", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCdkStock bizCdkStock)
    {
        return toAjax(bizCdkStockService.insertBizCdkStock(bizCdkStock));
    }

    /**
     * 修改CDK库存
     */
    @PreAuthorize("@ss.hasPermi('purchase:stock:edit')")
    @Log(title = "CDK库存", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCdkStock bizCdkStock)
    {
        return toAjax(bizCdkStockService.updateBizCdkStock(bizCdkStock));
    }

    /**
     * 删除CDK库存
     */
    @PreAuthorize("@ss.hasPermi('purchase:stock:remove')")
    @Log(title = "CDK库存", businessType = BusinessType.DELETE)
	@DeleteMapping("/{stockIds}")
    public AjaxResult remove(@PathVariable Long[] stockIds)
    {
        return toAjax(bizCdkStockService.deleteBizCdkStockByStockIds(stockIds));
    }
}
