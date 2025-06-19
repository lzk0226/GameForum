package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.forum.domain.BizPostLike;
import com.ruoyi.forum.service.IBizPostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 帖子点赞Controller
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/post_like")
public class BizPostLikeController extends BaseController
{
    @Autowired
    private IBizPostLikeService bizPostLikeService;

    /**
     * 查询帖子点赞列表
     */
    @PreAuthorize("@ss.hasPermi('forum:post_like:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPostLike bizPostLike)
    {
        startPage();
        List<BizPostLike> list = bizPostLikeService.selectBizPostLikeList(bizPostLike);
        return getDataTable(list);
    }

    /**
     * 导出帖子点赞列表
     */
    @PreAuthorize("@ss.hasPermi('forum:post_like:export')")
    @Log(title = "帖子点赞", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPostLike bizPostLike)
    {
        List<BizPostLike> list = bizPostLikeService.selectBizPostLikeList(bizPostLike);
        ExcelUtil<BizPostLike> util = new ExcelUtil<BizPostLike>(BizPostLike.class);
        util.exportExcel(response, list, "帖子点赞数据");
    }

    /**
     * 获取帖子点赞详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:post_like:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(bizPostLikeService.selectBizPostLikeByUserId(userId));
    }

    /**
     * 新增帖子点赞
     */
    @PreAuthorize("@ss.hasPermi('forum:post_like:add')")
    @Log(title = "帖子点赞", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizPostLike bizPostLike)
    {
        return toAjax(bizPostLikeService.insertBizPostLike(bizPostLike));
    }

    /**
     * 修改帖子点赞
     */
    @PreAuthorize("@ss.hasPermi('forum:post_like:edit')")
    @Log(title = "帖子点赞", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizPostLike bizPostLike)
    {
        return toAjax(bizPostLikeService.updateBizPostLike(bizPostLike));
    }

    /**
     * 删除帖子点赞
     */
    @PreAuthorize("@ss.hasPermi('forum:post_like:remove')")
    @Log(title = "帖子点赞", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(bizPostLikeService.deleteBizPostLikeByUserIds(userIds));
    }
}
