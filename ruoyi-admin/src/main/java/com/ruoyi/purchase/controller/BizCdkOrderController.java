package com.ruoyi.purchase.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.purchase.domain.BizCdkOrder;
import com.ruoyi.purchase.service.IBizCdkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * CDK订单Controller
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/purchase/order")
public class BizCdkOrderController extends BaseController
{
    @Autowired
    private IBizCdkOrderService bizCdkOrderService;

    /**
     * 查询CDK订单列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCdkOrder bizCdkOrder)
    {
        startPage();
        List<BizCdkOrder> list = bizCdkOrderService.selectBizCdkOrderList(bizCdkOrder);
        return getDataTable(list);
    }

    /**
     * 导出CDK订单列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:order:export')")
    @Log(title = "CDK订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCdkOrder bizCdkOrder)
    {
        List<BizCdkOrder> list = bizCdkOrderService.selectBizCdkOrderList(bizCdkOrder);
        ExcelUtil<BizCdkOrder> util = new ExcelUtil<BizCdkOrder>(BizCdkOrder.class);
        util.exportExcel(response, list, "CDK订单数据");
    }

    /**
     * 获取CDK订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(bizCdkOrderService.selectBizCdkOrderByOrderId(orderId));
    }

    /**
     * 新增CDK订单
     */
    @PreAuthorize("@ss.hasPermi('purchase:order:add')")
    @Log(title = "CDK订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCdkOrder bizCdkOrder)
    {
        return toAjax(bizCdkOrderService.insertBizCdkOrder(bizCdkOrder));
    }

    /**
     * 修改CDK订单
     */
    @PreAuthorize("@ss.hasPermi('purchase:order:edit')")
    @Log(title = "CDK订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCdkOrder bizCdkOrder)
    {
        return toAjax(bizCdkOrderService.updateBizCdkOrder(bizCdkOrder));
    }

    /**
     * 删除CDK订单
     */
    @PreAuthorize("@ss.hasPermi('purchase:order:remove')")
    @Log(title = "CDK订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(bizCdkOrderService.deleteBizCdkOrderByOrderIds(orderIds));
    }
}
