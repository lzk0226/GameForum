package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.forum.domain.BizPost;
import com.ruoyi.forum.service.IBizPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 论坛帖子Controller
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/post")
public class BizPostController extends BaseController
{
    @Autowired
    private IBizPostService bizPostService;

    /**
     * 查询论坛帖子列表
     */
    @PreAuthorize("@ss.hasPermi('forum:post:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPost bizPost)
    {
        startPage();
        List<BizPost> list = bizPostService.selectBizPostList(bizPost);
        return getDataTable(list);
    }

    /**
     * 导出论坛帖子列表
     */
    @PreAuthorize("@ss.hasPermi('forum:post:export')")
    @Log(title = "论坛帖子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPost bizPost)
    {
        List<BizPost> list = bizPostService.selectBizPostList(bizPost);
        ExcelUtil<BizPost> util = new ExcelUtil<BizPost>(BizPost.class);
        util.exportExcel(response, list, "论坛帖子数据");
    }

    /**
     * 获取论坛帖子详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:post:query')")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable("postId") Long postId)
    {
        return success(bizPostService.selectBizPostByPostId(postId));
    }

    /**
     * 新增论坛帖子
     */
    @PreAuthorize("@ss.hasPermi('forum:post:add')")
    @Log(title = "论坛帖子", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizPost bizPost)
    {
        return toAjax(bizPostService.insertBizPost(bizPost));
    }

    /**
     * 修改论坛帖子
     */
    @PreAuthorize("@ss.hasPermi('forum:post:edit')")
    @Log(title = "论坛帖子", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizPost bizPost)
    {
        return toAjax(bizPostService.updateBizPost(bizPost));
    }

    /**
     * 删除论坛帖子
     */
    @PreAuthorize("@ss.hasPermi('forum:post:remove')")
    @Log(title = "论坛帖子", businessType = BusinessType.DELETE)
	@DeleteMapping("/{postIds}")
    public AjaxResult remove(@PathVariable Long[] postIds)
    {
        return toAjax(bizPostService.deleteBizPostByPostIds(postIds));
    }
}
