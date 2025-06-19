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
import com.ruoyi.purchase.domain.BizUserBalance;
import com.ruoyi.purchase.service.IBizUserBalanceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户余额Controller
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/purchase/balance")
public class BizUserBalanceController extends BaseController
{
    @Autowired
    private IBizUserBalanceService bizUserBalanceService;

    /**
     * 查询用户余额列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:balance:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizUserBalance bizUserBalance)
    {
        startPage();
        List<BizUserBalance> list = bizUserBalanceService.selectBizUserBalanceList(bizUserBalance);
        return getDataTable(list);
    }

    /**
     * 导出用户余额列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:balance:export')")
    @Log(title = "用户余额", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizUserBalance bizUserBalance)
    {
        List<BizUserBalance> list = bizUserBalanceService.selectBizUserBalanceList(bizUserBalance);
        ExcelUtil<BizUserBalance> util = new ExcelUtil<BizUserBalance>(BizUserBalance.class);
        util.exportExcel(response, list, "用户余额数据");
    }

    /**
     * 获取用户余额详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:balance:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(bizUserBalanceService.selectBizUserBalanceByUserId(userId));
    }

    /**
     * 新增用户余额
     */
    @PreAuthorize("@ss.hasPermi('purchase:balance:add')")
    @Log(title = "用户余额", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizUserBalance bizUserBalance)
    {
        return toAjax(bizUserBalanceService.insertBizUserBalance(bizUserBalance));
    }

    /**
     * 修改用户余额
     */
    @PreAuthorize("@ss.hasPermi('purchase:balance:edit')")
    @Log(title = "用户余额", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizUserBalance bizUserBalance)
    {
        return toAjax(bizUserBalanceService.updateBizUserBalance(bizUserBalance));
    }

    /**
     * 删除用户余额
     */
    @PreAuthorize("@ss.hasPermi('purchase:balance:remove')")
    @Log(title = "用户余额", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(bizUserBalanceService.deleteBizUserBalanceByUserIds(userIds));
    }
}
