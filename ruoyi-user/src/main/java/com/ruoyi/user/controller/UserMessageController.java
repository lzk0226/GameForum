package com.ruoyi.user.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.R.R;
import com.ruoyi.user.domain.UserMessage;
import com.ruoyi.user.emun.ResultCodeEnum;
import com.ruoyi.user.service.IUserMessageService;
import com.ruoyi.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 用户消息 Controller
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
@RestController
@RequestMapping("/user/message")
@Validated
public class UserMessageController {

    @Autowired
    private IUserMessageService userMessageService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 验证 Token 并获取用户 ID
     */
    private Long validateTokenAndGetUserId(String authHeader) {
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return null;
        }
        return jwtUtils.getUserIdFromToken(token);
    }

    /**
     * 获取当前用户的消息列表
     */
    @GetMapping("/list")
    public R<?> getMessageList(
            UserMessage userMessage,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        userMessage.setReceiverId(userId);
        userMessage.setDelFlag("0");
        List<UserMessage> messageList = userMessageService.selectMessageList(userMessage);
        return R.ok(messageList);
    }

    /**
     * 获取当前用户的未读消息列表
     */
    @GetMapping("/unread")
    public R<?> getUnreadMessageList(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        List<UserMessage> unreadMessages = userMessageService.selectUnreadMessageList(userId);
        return R.ok(unreadMessages);
    }

    /**
     * 获取消息详情
     */
    @GetMapping("/{messageId}")
    public R<?> getMessageDetail(
            @PathVariable("messageId") @NotNull(message = "消息ID不能为空") Long messageId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        UserMessage message = userMessageService.selectMessageById(messageId);
        if (message == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "消息不存在");
        }

        // 检查消息是否属于当前用户
        if (!userId.equals(message.getReceiverId())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "无权查看该消息");
        }

        // 如果消息未读，自动标记为已读
        if ("0".equals(message.getIsRead())) {
            userMessageService.markAsRead(messageId, userId);
            message.setIsRead("1");
        }

        return R.ok(message);
    }

    /**
     * 获取当前用户的未读消息数
     */
    @GetMapping("/count/unread")
    public R<?> getUnreadCount(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        int count = userMessageService.countUnreadMessages(userId);
        return R.ok(count);
    }

    /**
     * 按类型统计未读消息数
     */
    @GetMapping("/count/unread/by-type")
    public R<?> getUnreadCountByType(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        Map<String, Integer> countByType = userMessageService.countUnreadByType(userId);
        return R.ok(countByType);
    }

    /**
     * 发送消息（通用接口，一般用于系统消息）
     */
    @PostMapping("/send")
    public R<?> sendMessage(
            @Valid @RequestBody UserMessage userMessage,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (userMessage.getReceiverId() == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "接收者ID不能为空");
        }

        if (StringUtils.isEmpty(userMessage.getMessageContent())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "消息内容不能为空");
        }

        userMessage.setSenderId(userId);
        int result = userMessageService.sendMessage(userMessage);
        return result > 0 ? R.ok("发送成功") : R.fail("发送失败");
    }

    /**
     * 发送系统消息（管理员专用）
     */
    @PostMapping("/send/system")
    public R<?> sendSystemMessage(
            @RequestParam("receiverId") @NotNull(message = "接收者ID不能为空") Long receiverId,
            @RequestParam("content") @NotBlank(message = "消息内容不能为空") String content,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        int result = userMessageService.sendSystemMessage(receiverId, content);
        return result > 0 ? R.ok("发送成功") : R.fail("发送失败");
    }

    /**
     * 标记单条消息为已读
     */
    @PutMapping("/read/{messageId}")
    public R<?> markAsRead(
            @PathVariable("messageId") @NotNull(message = "消息ID不能为空") Long messageId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 检查消息是否存在且属于当前用户
        UserMessage message = userMessageService.selectMessageById(messageId);
        if (message == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "消息不存在");
        }

        if (!userId.equals(message.getReceiverId())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "无权操作该消息");
        }

        int result = userMessageService.markAsRead(messageId, userId);
        return result > 0 ? R.ok("标记成功") : R.fail("标记失败");
    }

    /**
     * 批量标记消息为已读
     */
    @PutMapping("/read/batch")
    public R<?> markAsReadBatch(
            @RequestBody List<Long> messageIds,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (messageIds == null || messageIds.isEmpty()) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "消息ID列表不能为空");
        }

        int result = userMessageService.markAsReadBatch(messageIds, userId);
        return result > 0 ? R.ok("批量标记成功") : R.fail("批量标记失败");
    }

    /**
     * 标记所有消息为已读
     */
    @PutMapping("/read/all")
    public R<?> markAllAsRead(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        int result = userMessageService.markAllAsRead(userId);
        return result > 0 ? R.ok("全部标记成功") : R.fail("全部标记失败");
    }

    /**
     * 按类型标记消息为已读
     */
    @PutMapping("/read/by-type")
    public R<?> markAsReadByType(
            @RequestParam("messageType") @NotBlank(message = "消息类型不能为空") String messageType,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 验证消息类型
        if (!isValidMessageType(messageType)) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "无效的消息类型");
        }

        int result = userMessageService.markAsReadByType(userId, messageType);
        return result > 0 ? R.ok("标记成功") : R.fail("标记失败");
    }

    /**
     * 删除单条消息
     */
    @DeleteMapping("/{messageId}")
    public R<?> deleteMessage(
            @PathVariable("messageId") @NotNull(message = "消息ID不能为空") Long messageId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 检查消息是否存在且属于当前用户
        UserMessage message = userMessageService.selectMessageById(messageId);
        if (message == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "消息不存在");
        }

        if (!userId.equals(message.getReceiverId())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "无权删除该消息");
        }

        int result = userMessageService.deleteMessage(messageId, userId);
        return result > 0 ? R.ok("删除成功") : R.fail("删除失败");
    }

    /**
     * 批量删除消息
     */
    @DeleteMapping("/batch")
    public R<?> deleteMessageBatch(
            @RequestBody List<Long> messageIds,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (messageIds == null || messageIds.isEmpty()) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "消息ID列表不能为空");
        }

        int result = userMessageService.deleteMessageBatch(messageIds, userId);
        return result > 0 ? R.ok("批量删除成功") : R.fail("批量删除失败");
    }

    /**
     * 清空已读消息
     */
    @DeleteMapping("/clear")
    public R<?> clearReadMessages(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        int result = userMessageService.clearReadMessages(userId);
        return result > 0 ? R.ok("清空成功") : R.fail("清空失败");
    }

    /**
     * 验证消息类型是否有效
     */
    private boolean isValidMessageType(String messageType) {
        // 0-点赞, 1-评论, 2-关注, 3-收藏, 4-系统消息
        return "0".equals(messageType) || "1".equals(messageType) ||
                "2".equals(messageType) || "3".equals(messageType) ||
                "4".equals(messageType);
    }
}