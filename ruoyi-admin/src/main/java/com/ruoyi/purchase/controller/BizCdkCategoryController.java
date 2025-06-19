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
import com.ruoyi.purchase.domain.BizCdkCategory;
import com.ruoyi.purchase.service.IBizCdkCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * CDK商品分类Controller
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/purchase/category")
public class BizCdkCategoryController extends BaseController
{
    @Autowired
    private IBizCdkCategoryService bizCdkCategoryService;

    /**
     * 查询CDK商品分类列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCdkCategory bizCdkCategory)
    {
        startPage();
        List<BizCdkCategory> list = bizCdkCategoryService.selectBizCdkCategoryList(bizCdkCategory);
        return getDataTable(list);
    }

    /**
     * 导出CDK商品分类列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:category:export')")
    @Log(title = "CDK商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCdkCategory bizCdkCategory)
    {
        List<BizCdkCategory> list = bizCdkCategoryService.selectBizCdkCategoryList(bizCdkCategory);
        ExcelUtil<BizCdkCategory> util = new ExcelUtil<BizCdkCategory>(BizCdkCategory.class);
        util.exportExcel(response, list, "CDK商品分类数据");
    }

    /**
     * 获取CDK商品分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return success(bizCdkCategoryService.selectBizCdkCategoryByCategoryId(categoryId));
    }

    /**
     * 新增CDK商品分类
     */
    @PreAuthorize("@ss.hasPermi('purchase:category:add')")
    @Log(title = "CDK商品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCdkCategory bizCdkCategory)
    {
        return toAjax(bizCdkCategoryService.insertBizCdkCategory(bizCdkCategory));
    }

    /**
     * 修改CDK商品分类
     */
    @PreAuthorize("@ss.hasPermi('purchase:category:edit')")
    @Log(title = "CDK商品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCdkCategory bizCdkCategory)
    {
        return toAjax(bizCdkCategoryService.updateBizCdkCategory(bizCdkCategory));
    }

    /**
     * 删除CDK商品分类
     */
    @PreAuthorize("@ss.hasPermi('purchase:category:remove')")
    @Log(title = "CDK商品分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(bizCdkCategoryService.deleteBizCdkCategoryByCategoryIds(categoryIds));
    }
}
