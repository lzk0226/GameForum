package com.ruoyi.user.service.impl;

import com.ruoyi.user.domain.User;
import com.ruoyi.user.domain.UserMessage;
import com.ruoyi.user.mapper.UserMapper;
import com.ruoyi.user.mapper.UserMessageMapper;
import com.ruoyi.user.service.IUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户消息Service业务层处理
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
@Service
public class UserMessageServiceImpl implements IUserMessageService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserMessage> selectMessageList(UserMessage userMessage) {
        return userMessageMapper.selectMessageList(userMessage);
    }

    @Override
    public List<UserMessage> selectUnreadMessageList(Long userId) {
        return userMessageMapper.selectUnreadMessageList(userId);
    }

    @Override
    public UserMessage selectMessageById(Long messageId) {
        return userMessageMapper.selectMessageById(messageId);
    }

    @Override
    public int countUnreadMessages(Long userId) {
        return userMessageMapper.countUnreadByUserId(userId);
    }

    @Override
    public Map<String, Integer> countUnreadByType(Long userId) {
        List<HashMap<String, Object>> result = userMessageMapper.countUnreadByType(userId);

        return result.stream()
                .collect(Collectors.toMap(
                        map -> (String) map.get("message_type"),
                        map -> ((Number) map.get("count")).intValue()
                ));
    }

    @Override
    @Transactional
    public int sendMessage(UserMessage userMessage) {
        if (userMessage.getIsRead() == null) {
            userMessage.setIsRead("0");
        }
        if (userMessage.getDelFlag() == null) {
            userMessage.setDelFlag("0");
        }
        return userMessageMapper.insertMessage(userMessage);
    }

    @Override
    public int sendPostLikeMessage(Long senderId, Long receiverId, Integer postId) {
        if (senderId.equals(receiverId)) {
            return 0; // 不给自己发消息
        }

        User sender = userMapper.selectUserById(senderId);
        if (sender == null) {
            return 0;
        }

        UserMessage message = new UserMessage();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setMessageType("0");
        message.setMessageContent(sender.getNickName() + " 点赞了你的帖子");
        message.setRelatedType("0");
        message.setRelatedId(postId);

        return sendMessage(message);
    }

    @Override
    public int sendCommentLikeMessage(Long senderId, Long receiverId, Integer commentId) {
        if (senderId.equals(receiverId)) {
            return 0;
        }

        User sender = userMapper.selectUserById(senderId);
        if (sender == null) {
            return 0;
        }

        UserMessage message = new UserMessage();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setMessageType("0");
        message.setMessageContent(sender.getNickName() + " 点赞了你的评论");
        message.setRelatedType("1");
        message.setRelatedId(commentId);

        return sendMessage(message);
    }

    @Override
    public int sendPostCommentMessage(Long senderId, Long receiverId, Integer postId) {
        if (senderId.equals(receiverId)) {
            return 0;
        }

        User sender = userMapper.selectUserById(senderId);
        if (sender == null) {
            return 0;
        }

        UserMessage message = new UserMessage();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setMessageType("1");
        message.setMessageContent(sender.getNickName() + " 评论了你的帖子");
        message.setRelatedType("0");
        message.setRelatedId(postId);

        return sendMessage(message);
    }

    @Override
    public int sendCommentReplyMessage(Long senderId, Long receiverId, Integer commentId) {
        if (senderId.equals(receiverId)) {
            return 0;
        }

        User sender = userMapper.selectUserById(senderId);
        if (sender == null) {
            return 0;
        }

        UserMessage message = new UserMessage();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setMessageType("1");
        message.setMessageContent(sender.getNickName() + " 回复了你的评论");
        message.setRelatedType("1");
        message.setRelatedId(commentId);

        return sendMessage(message);
    }

    @Override
    public int sendSystemMessage(Long receiverId, String content) {
        UserMessage message = new UserMessage();
        message.setReceiverId(receiverId);
        message.setMessageType("4");
        message.setMessageContent(content);

        return sendMessage(message);
    }

    @Override
    @Transactional
    public int markAsRead(Long messageId, Long userId) {
        return userMessageMapper.markAsRead(messageId, userId);
    }

    @Override
    @Transactional
    public int markAsReadBatch(List<Long> messageIds, Long userId) {
        if (messageIds == null || messageIds.isEmpty()) {
            return 0;
        }
        return userMessageMapper.markAsReadBatch(userId, messageIds);
    }

    @Override
    @Transactional
    public int markAllAsRead(Long userId) {
        return userMessageMapper.markAllAsRead(userId);
    }

    @Override
    @Transactional
    public int markAsReadByType(Long userId, String messageType) {
        return userMessageMapper.markAsReadByType(userId, messageType);
    }

    @Override
    @Transactional
    public int deleteMessage(Long messageId, Long userId) {
        return userMessageMapper.deleteMessage(messageId, userId);
    }

    @Override
    @Transactional
    public int deleteMessageBatch(List<Long> messageIds, Long userId) {
        if (messageIds == null || messageIds.isEmpty()) {
            return 0;
        }
        return userMessageMapper.deleteMessageBatch(userId, messageIds);
    }

    @Override
    @Transactional
    public int clearReadMessages(Long userId) {
        return userMessageMapper.clearReadMessages(userId);
    }
}
