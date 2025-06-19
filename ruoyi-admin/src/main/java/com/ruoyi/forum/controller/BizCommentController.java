package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.forum.domain.BizComment;
import com.ruoyi.forum.service.IBizCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 评论Controller
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/comment")
public class BizCommentController extends BaseController
{
    @Autowired
    private IBizCommentService bizCommentService;

    /**
     * 查询评论列表
     */
    @PreAuthorize("@ss.hasPermi('forum:comment:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizComment bizComment)
    {
        startPage();
        List<BizComment> list = bizCommentService.selectBizCommentList(bizComment);
        return getDataTable(list);
    }

    /**
     * 导出评论列表
     */
    @PreAuthorize("@ss.hasPermi('forum:comment:export')")
    @Log(title = "评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizComment bizComment)
    {
        List<BizComment> list = bizCommentService.selectBizCommentList(bizComment);
        ExcelUtil<BizComment> util = new ExcelUtil<BizComment>(BizComment.class);
        util.exportExcel(response, list, "评论数据");
    }

    /**
     * 获取评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:comment:query')")
    @GetMapping(value = "/{commentId}")
    public AjaxResult getInfo(@PathVariable("commentId") Long commentId)
    {
        return success(bizCommentService.selectBizCommentByCommentId(commentId));
    }

    /**
     * 新增评论
     */
    @PreAuthorize("@ss.hasPermi('forum:comment:add')")
    @Log(title = "评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizComment bizComment)
    {
        return toAjax(bizCommentService.insertBizComment(bizComment));
    }

    /**
     * 修改评论
     */
    @PreAuthorize("@ss.hasPermi('forum:comment:edit')")
    @Log(title = "评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizComment bizComment)
    {
        return toAjax(bizCommentService.updateBizComment(bizComment));
    }

    /**
     * 删除评论
     */
    @PreAuthorize("@ss.hasPermi('forum:comment:remove')")
    @Log(title = "评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{commentIds}")
    public AjaxResult remove(@PathVariable Long[] commentIds)
    {
        return toAjax(bizCommentService.deleteBizCommentByCommentIds(commentIds));
    }
}
