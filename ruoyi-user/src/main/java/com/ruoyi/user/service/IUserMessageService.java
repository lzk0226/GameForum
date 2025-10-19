package com.ruoyi.user.service;

import com.ruoyi.user.domain.UserMessage;

import java.util.List;
import java.util.Map;

/**
 * 用户消息Service接口
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
public interface IUserMessageService {

    /**
     * 查询消息列表
     *
     * @param userMessage 查询条件
     * @return 消息列表
     */
    List<UserMessage> selectMessageList(UserMessage userMessage);

    /**
     * 查询未读消息列表
     *
     * @param userId 用户ID
     * @return 未读消息列表
     */
    List<UserMessage> selectUnreadMessageList(Long userId);

    /**
     * 查询消息详情
     *
     * @param messageId 消息ID
     * @return 消息详情
     */
    UserMessage selectMessageById(Long messageId);

    /**
     * 统计未读消息数
     *
     * @param userId 用户ID
     * @return 未读消息数
     */
    int countUnreadMessages(Long userId);

    /**
     * 按类型统计未读消息数
     *
     * @param userId 用户ID
     * @return 各类型未读消息数
     */
    Map<String, Integer> countUnreadByType(Long userId);

    /**
     * 发送消息（通用方法）
     *
     * @param userMessage 消息对象
     * @return 结果
     */
    int sendMessage(UserMessage userMessage);

    /**
     * 发送点赞帖子消息
     *
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param postId 帖子ID
     * @return 结果
     */
    int sendPostLikeMessage(Long senderId, Long receiverId, Integer postId);

    /**
     * 发送点赞评论消息
     *
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param commentId 评论ID
     * @return 结果
     */
    int sendCommentLikeMessage(Long senderId, Long receiverId, Integer commentId);

    /**
     * 发送评论帖子消息
     *
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param postId 帖子ID
     * @return 结果
     */
    int sendPostCommentMessage(Long senderId, Long receiverId, Integer postId);

    /**
     * 发送回复评论消息
     *
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param commentId 评论ID
     * @return 结果
     */
    int sendCommentReplyMessage(Long senderId, Long receiverId, Integer commentId);

    /**
     * 发送系统消息
     *
     * @param receiverId 接收者ID
     * @param content 消息内容
     * @return 结果
     */
    int sendSystemMessage(Long receiverId, String content);

    /**
     * 标记为已读
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 结果
     */
    int markAsRead(Long messageId, Long userId);

    /**
     * 批量标记为已读
     *
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 结果
     */
    int markAsReadBatch(List<Long> messageIds, Long userId);

    /**
     * 标记所有消息为已读
     *
     * @param userId 用户ID
     * @return 结果
     */
    int markAllAsRead(Long userId);

    /**
     * 按类型标记为已读
     *
     * @param userId 用户ID
     * @param messageType 消息类型
     * @return 结果
     */
    int markAsReadByType(Long userId, String messageType);

    /**
     * 删除消息
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 结果
     */
    int deleteMessage(Long messageId, Long userId);

    /**
     * 批量删除消息
     *
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 结果
     */
    int deleteMessageBatch(List<Long> messageIds, Long userId);

    /**
     * 清空已读消息
     *
     * @param userId 用户ID
     * @return 结果
     */
    int clearReadMessages(Long userId);
}
