package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.forum.domain.BizCommentLike;
import com.ruoyi.forum.service.IBizCommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 评论点赞Controller
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/comment_like")
public class BizCommentLikeController extends BaseController
{
    @Autowired
    private IBizCommentLikeService bizCommentLikeService;

    /**
     * 查询评论点赞列表
     */
    @PreAuthorize("@ss.hasPermi('forum:comment_like:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCommentLike bizCommentLike)
    {
        startPage();
        List<BizCommentLike> list = bizCommentLikeService.selectBizCommentLikeList(bizCommentLike);
        return getDataTable(list);
    }

    /**
     * 导出评论点赞列表
     */
    @PreAuthorize("@ss.hasPermi('forum:comment_like:export')")
    @Log(title = "评论点赞", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCommentLike bizCommentLike)
    {
        List<BizCommentLike> list = bizCommentLikeService.selectBizCommentLikeList(bizCommentLike);
        ExcelUtil<BizCommentLike> util = new ExcelUtil<BizCommentLike>(BizCommentLike.class);
        util.exportExcel(response, list, "评论点赞数据");
    }

    /**
     * 获取评论点赞详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:comment_like:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(bizCommentLikeService.selectBizCommentLikeByUserId(userId));
    }

    /**
     * 新增评论点赞
     */
    @PreAuthorize("@ss.hasPermi('forum:comment_like:add')")
    @Log(title = "评论点赞", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCommentLike bizCommentLike)
    {
        return toAjax(bizCommentLikeService.insertBizCommentLike(bizCommentLike));
    }

    /**
     * 修改评论点赞
     */
    @PreAuthorize("@ss.hasPermi('forum:comment_like:edit')")
    @Log(title = "评论点赞", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCommentLike bizCommentLike)
    {
        return toAjax(bizCommentLikeService.updateBizCommentLike(bizCommentLike));
    }

    /**
     * 删除评论点赞
     */
    @PreAuthorize("@ss.hasPermi('forum:comment_like:remove')")
    @Log(title = "评论点赞", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(bizCommentLikeService.deleteBizCommentLikeByUserIds(userIds));
    }
}
