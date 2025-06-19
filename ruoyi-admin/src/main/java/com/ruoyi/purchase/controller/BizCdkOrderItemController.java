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
import com.ruoyi.purchase.domain.BizCdkOrderItem;
import com.ruoyi.purchase.service.IBizCdkOrderItemService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * CDK订单项Controller
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/purchase/item")
public class BizCdkOrderItemController extends BaseController
{
    @Autowired
    private IBizCdkOrderItemService bizCdkOrderItemService;

    /**
     * 查询CDK订单项列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCdkOrderItem bizCdkOrderItem)
    {
        startPage();
        List<BizCdkOrderItem> list = bizCdkOrderItemService.selectBizCdkOrderItemList(bizCdkOrderItem);
        return getDataTable(list);
    }

    /**
     * 导出CDK订单项列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:item:export')")
    @Log(title = "CDK订单项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCdkOrderItem bizCdkOrderItem)
    {
        List<BizCdkOrderItem> list = bizCdkOrderItemService.selectBizCdkOrderItemList(bizCdkOrderItem);
        ExcelUtil<BizCdkOrderItem> util = new ExcelUtil<BizCdkOrderItem>(BizCdkOrderItem.class);
        util.exportExcel(response, list, "CDK订单项数据");
    }

    /**
     * 获取CDK订单项详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:item:query')")
    @GetMapping(value = "/{itemId}")
    public AjaxResult getInfo(@PathVariable("itemId") Long itemId)
    {
        return success(bizCdkOrderItemService.selectBizCdkOrderItemByItemId(itemId));
    }

    /**
     * 新增CDK订单项
     */
    @PreAuthorize("@ss.hasPermi('purchase:item:add')")
    @Log(title = "CDK订单项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCdkOrderItem bizCdkOrderItem)
    {
        return toAjax(bizCdkOrderItemService.insertBizCdkOrderItem(bizCdkOrderItem));
    }

    /**
     * 修改CDK订单项
     */
    @PreAuthorize("@ss.hasPermi('purchase:item:edit')")
    @Log(title = "CDK订单项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCdkOrderItem bizCdkOrderItem)
    {
        return toAjax(bizCdkOrderItemService.updateBizCdkOrderItem(bizCdkOrderItem));
    }

    /**
     * 删除CDK订单项
     */
    @PreAuthorize("@ss.hasPermi('purchase:item:remove')")
    @Log(title = "CDK订单项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{itemIds}")
    public AjaxResult remove(@PathVariable Long[] itemIds)
    {
        return toAjax(bizCdkOrderItemService.deleteBizCdkOrderItemByItemIds(itemIds));
    }
}
