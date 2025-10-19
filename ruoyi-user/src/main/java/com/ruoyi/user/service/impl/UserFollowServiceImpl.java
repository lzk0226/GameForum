package com.ruoyi.user.service.impl;

import com.ruoyi.user.domain.User;
import com.ruoyi.user.domain.UserFollow;
import com.ruoyi.user.domain.UserMessage;
import com.ruoyi.user.mapper.UserFollowMapper;
import com.ruoyi.user.mapper.UserMapper;
import com.ruoyi.user.mapper.UserMessageMapper;
import com.ruoyi.user.service.IUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户关注Service业务层处理
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
@Service
public class UserFollowServiceImpl implements IUserFollowService {

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public List<UserFollow> selectFollowingList(Long userId) {
        return userFollowMapper.selectFollowingListByUserId(userId);
    }

    @Override
    public List<UserFollow> selectFollowerList(Long userId) {
        return userFollowMapper.selectFollowerListByUserId(userId);
    }

    @Override
    public List<UserFollow> selectMutualFollowList(Long userId) {
        return userFollowMapper.selectMutualFollowList(userId);
    }

    @Override
    public boolean checkUserFollowed(Long followerId, Long followingId) {
        return userFollowMapper.checkUserFollowed(followerId, followingId) > 0;
    }

    @Override
    public boolean checkMutualFollow(Long userId1, Long userId2) {
        return userFollowMapper.checkMutualFollow(userId1, userId2);
    }

    @Override
    @Transactional
    public int followUser(Long followerId, Long followingId) {
        // 不能关注自己
        if (followerId.equals(followingId)) {
            return 0;
        }

        // 检查是否已关注
        if (checkUserFollowed(followerId, followingId)) {
            return 0;
        }

        // 添加关注记录
        int result = userFollowMapper.insertFollow(followerId, followingId);

        // 发送消息通知
        if (result > 0) {
            sendFollowMessage(followerId, followingId);
        }

        return result;
    }

    @Override
    @Transactional
    public int unfollowUser(Long followerId, Long followingId) {
        return userFollowMapper.deleteFollow(followerId, followingId);
    }

    @Override
    public int countFollowing(Long userId) {
        return userFollowMapper.countFollowingByUserId(userId);
    }

    @Override
    public int countFollower(Long userId) {
        return userFollowMapper.countFollowerByUserId(userId);
    }

    @Override
    public Map<Long, Boolean> checkUserFollowedBatch(Long followerId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return new HashMap<>();
        }

        List<HashMap<String, Object>> result = userFollowMapper.checkUserFollowedBatch(followerId, userIds);

        return result.stream()
                .collect(Collectors.toMap(
                        map -> ((Number) map.get("user_id")).longValue(),
                        map -> true
                ));
    }

    /**
     * 发送关注消息通知
     */
    private void sendFollowMessage(Long followerId, Long followingId) {
        try {
            // 获取关注者信息
            User follower = userMapper.selectUserById(followerId);
            if (follower == null) {
                return;
            }

            // 创建消息
            UserMessage message = new UserMessage();
            message.setReceiverId(followingId);
            message.setSenderId(followerId);
            message.setMessageType("2"); // 关注类型
            message.setMessageContent(follower.getNickName() + " 关注了你");
            message.setIsRead("0");
            message.setDelFlag("0");

            userMessageMapper.insertMessage(message);
        } catch (Exception e) {
            // 消息发送失败不影响主流程
            e.printStackTrace();
        }
    }
}
