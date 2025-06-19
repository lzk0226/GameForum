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
import com.ruoyi.purchase.domain.BizCdkProduct;
import com.ruoyi.purchase.service.IBizCdkProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * CDK商品Controller
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/purchase/product")
public class BizCdkProductController extends BaseController
{
    @Autowired
    private IBizCdkProductService bizCdkProductService;

    /**
     * 查询CDK商品列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCdkProduct bizCdkProduct)
    {
        startPage();
        List<BizCdkProduct> list = bizCdkProductService.selectBizCdkProductList(bizCdkProduct);
        return getDataTable(list);
    }

    /**
     * 导出CDK商品列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:product:export')")
    @Log(title = "CDK商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCdkProduct bizCdkProduct)
    {
        List<BizCdkProduct> list = bizCdkProductService.selectBizCdkProductList(bizCdkProduct);
        ExcelUtil<BizCdkProduct> util = new ExcelUtil<BizCdkProduct>(BizCdkProduct.class);
        util.exportExcel(response, list, "CDK商品数据");
    }

    /**
     * 获取CDK商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:product:query')")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable("productId") Long productId)
    {
        return success(bizCdkProductService.selectBizCdkProductByProductId(productId));
    }

    /**
     * 新增CDK商品
     */
    @PreAuthorize("@ss.hasPermi('purchase:product:add')")
    @Log(title = "CDK商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCdkProduct bizCdkProduct)
    {
        return toAjax(bizCdkProductService.insertBizCdkProduct(bizCdkProduct));
    }

    /**
     * 修改CDK商品
     */
    @PreAuthorize("@ss.hasPermi('purchase:product:edit')")
    @Log(title = "CDK商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCdkProduct bizCdkProduct)
    {
        return toAjax(bizCdkProductService.updateBizCdkProduct(bizCdkProduct));
    }

    /**
     * 删除CDK商品
     */
    @PreAuthorize("@ss.hasPermi('purchase:product:remove')")
    @Log(title = "CDK商品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(bizCdkProductService.deleteBizCdkProductByProductIds(productIds));
    }
}
