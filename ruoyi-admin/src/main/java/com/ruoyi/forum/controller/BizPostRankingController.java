package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.forum.domain.BizPostRanking;
import com.ruoyi.forum.service.IBizPostRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 帖子排名Controller
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/ranking")
public class BizPostRankingController extends BaseController
{
    @Autowired
    private IBizPostRankingService bizPostRankingService;

    /**
     * 查询帖子排名列表
     */
    @PreAuthorize("@ss.hasPermi('forum:ranking:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPostRanking bizPostRanking)
    {
        startPage();
        List<BizPostRanking> list = bizPostRankingService.selectBizPostRankingList(bizPostRanking);
        return getDataTable(list);
    }

    /**
     * 导出帖子排名列表
     */
    @PreAuthorize("@ss.hasPermi('forum:ranking:export')")
    @Log(title = "帖子排名", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPostRanking bizPostRanking)
    {
        List<BizPostRanking> list = bizPostRankingService.selectBizPostRankingList(bizPostRanking);
        ExcelUtil<BizPostRanking> util = new ExcelUtil<BizPostRanking>(BizPostRanking.class);
        util.exportExcel(response, list, "帖子排名数据");
    }

    /**
     * 获取帖子排名详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:ranking:query')")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable("postId") Long postId)
    {
        return success(bizPostRankingService.selectBizPostRankingByPostId(postId));
    }

    /**
     * 新增帖子排名
     */
    @PreAuthorize("@ss.hasPermi('forum:ranking:add')")
    @Log(title = "帖子排名", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizPostRanking bizPostRanking)
    {
        return toAjax(bizPostRankingService.insertBizPostRanking(bizPostRanking));
    }

    /**
     * 修改帖子排名
     */
    @PreAuthorize("@ss.hasPermi('forum:ranking:edit')")
    @Log(title = "帖子排名", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizPostRanking bizPostRanking)
    {
        return toAjax(bizPostRankingService.updateBizPostRanking(bizPostRanking));
    }

    /**
     * 删除帖子排名
     */
    @PreAuthorize("@ss.hasPermi('forum:ranking:remove')")
    @Log(title = "帖子排名", businessType = BusinessType.DELETE)
	@DeleteMapping("/{postIds}")
    public AjaxResult remove(@PathVariable Long[] postIds)
    {
        return toAjax(bizPostRankingService.deleteBizPostRankingByPostIds(postIds));
    }
}
