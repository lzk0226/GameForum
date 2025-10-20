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
import com.ruoyi.forum.domain.BizUserMessage;
import com.ruoyi.forum.service.IBizUserMessageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户消息Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/forum/message")
public class BizUserMessageController extends BaseController
{
    @Autowired
    private IBizUserMessageService bizUserMessageService;

    /**
     * 查询用户消息列表
     */
    @PreAuthorize("@ss.hasPermi('forum:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizUserMessage bizUserMessage)
    {
        startPage();
        List<BizUserMessage> list = bizUserMessageService.selectBizUserMessageList(bizUserMessage);
        return getDataTable(list);
    }

    /**
     * 导出用户消息列表
     */
    @PreAuthorize("@ss.hasPermi('forum:message:export')")
    @Log(title = "用户消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizUserMessage bizUserMessage)
    {
        List<BizUserMessage> list = bizUserMessageService.selectBizUserMessageList(bizUserMessage);
        ExcelUtil<BizUserMessage> util = new ExcelUtil<BizUserMessage>(BizUserMessage.class);
        util.exportExcel(response, list, "用户消息数据");
    }

    /**
     * 获取用户消息详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:message:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        return success(bizUserMessageService.selectBizUserMessageByMessageId(messageId));
    }

    /**
     * 新增用户消息
     */
    @PreAuthorize("@ss.hasPermi('forum:message:add')")
    @Log(title = "用户消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizUserMessage bizUserMessage)
    {
        return toAjax(bizUserMessageService.insertBizUserMessage(bizUserMessage));
    }

    /**
     * 修改用户消息
     */
    @PreAuthorize("@ss.hasPermi('forum:message:edit')")
    @Log(title = "用户消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizUserMessage bizUserMessage)
    {
        return toAjax(bizUserMessageService.updateBizUserMessage(bizUserMessage));
    }

    /**
     * 删除用户消息
     */
    @PreAuthorize("@ss.hasPermi('forum:message:remove')")
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        return toAjax(bizUserMessageService.deleteBizUserMessageByMessageIds(messageIds));
    }
}
