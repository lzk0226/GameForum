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
import com.ruoyi.purchase.domain.BizProductCategory;
import com.ruoyi.purchase.service.IBizProductCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品分类关联Controller
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/purchase/productcategory")
public class BizProductCategoryController extends BaseController
{
    @Autowired
    private IBizProductCategoryService bizProductCategoryService;

    /**
     * 查询商品分类关联列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:productcategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizProductCategory bizProductCategory)
    {
        startPage();
        List<BizProductCategory> list = bizProductCategoryService.selectBizProductCategoryList(bizProductCategory);
        return getDataTable(list);
    }

    /**
     * 导出商品分类关联列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:productcategory:export')")
    @Log(title = "商品分类关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizProductCategory bizProductCategory)
    {
        List<BizProductCategory> list = bizProductCategoryService.selectBizProductCategoryList(bizProductCategory);
        ExcelUtil<BizProductCategory> util = new ExcelUtil<BizProductCategory>(BizProductCategory.class);
        util.exportExcel(response, list, "商品分类关联数据");
    }

    /**
     * 获取商品分类关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:productcategory:query')")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable("productId") Long productId)
    {
        return success(bizProductCategoryService.selectBizProductCategoryByProductId(productId));
    }

    /**
     * 新增商品分类关联
     */
    @PreAuthorize("@ss.hasPermi('purchase:productcategory:add')")
    @Log(title = "商品分类关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizProductCategory bizProductCategory)
    {
        return toAjax(bizProductCategoryService.insertBizProductCategory(bizProductCategory));
    }

    /**
     * 修改商品分类关联
     */
    @PreAuthorize("@ss.hasPermi('purchase:productcategory:edit')")
    @Log(title = "商品分类关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizProductCategory bizProductCategory)
    {
        return toAjax(bizProductCategoryService.updateBizProductCategory(bizProductCategory));
    }

    /**
     * 删除商品分类关联
     */
    @PreAuthorize("@ss.hasPermi('purchase:productcategory:remove')")
    @Log(title = "商品分类关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(bizProductCategoryService.deleteBizProductCategoryByProductIds(productIds));
    }
}
