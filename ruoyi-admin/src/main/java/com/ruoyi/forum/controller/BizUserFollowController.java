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
import com.ruoyi.forum.domain.BizUserFollow;
import com.ruoyi.forum.service.IBizUserFollowService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户关注Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/forum/follow")
public class BizUserFollowController extends BaseController
{
    @Autowired
    private IBizUserFollowService bizUserFollowService;

    /**
     * 查询用户关注列表
     */
    @PreAuthorize("@ss.hasPermi('forum:follow:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizUserFollow bizUserFollow)
    {
        startPage();
        List<BizUserFollow> list = bizUserFollowService.selectBizUserFollowList(bizUserFollow);
        return getDataTable(list);
    }

    /**
     * 导出用户关注列表
     */
    @PreAuthorize("@ss.hasPermi('forum:follow:export')")
    @Log(title = "用户关注", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizUserFollow bizUserFollow)
    {
        List<BizUserFollow> list = bizUserFollowService.selectBizUserFollowList(bizUserFollow);
        ExcelUtil<BizUserFollow> util = new ExcelUtil<BizUserFollow>(BizUserFollow.class);
        util.exportExcel(response, list, "用户关注数据");
    }

    /**
     * 获取用户关注详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:follow:query')")
    @GetMapping(value = "/{followerId}")
    public AjaxResult getInfo(@PathVariable("followerId") Long followerId)
    {
        return success(bizUserFollowService.selectBizUserFollowByFollowerId(followerId));
    }

    /**
     * 新增用户关注
     */
    @PreAuthorize("@ss.hasPermi('forum:follow:add')")
    @Log(title = "用户关注", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizUserFollow bizUserFollow)
    {
        return toAjax(bizUserFollowService.insertBizUserFollow(bizUserFollow));
    }

    /**
     * 修改用户关注
     */
    @PreAuthorize("@ss.hasPermi('forum:follow:edit')")
    @Log(title = "用户关注", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizUserFollow bizUserFollow)
    {
        return toAjax(bizUserFollowService.updateBizUserFollow(bizUserFollow));
    }

    /**
     * 删除用户关注
     */
    @PreAuthorize("@ss.hasPermi('forum:follow:remove')")
    @Log(title = "用户关注", businessType = BusinessType.DELETE)
	@DeleteMapping("/{followerIds}")
    public AjaxResult remove(@PathVariable Long[] followerIds)
    {
        return toAjax(bizUserFollowService.deleteBizUserFollowByFollowerIds(followerIds));
    }
}
