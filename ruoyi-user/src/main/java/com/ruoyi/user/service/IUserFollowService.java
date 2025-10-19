package com.ruoyi.user.service;

import com.ruoyi.user.domain.UserFollow;

import java.util.List;
import java.util.Map;

/**
 * 用户关注Service接口
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
public interface IUserFollowService {

    /**
     * 查询关注列表
     *
     * @param userId 用户ID
     * @return 关注列表
     */
    List<UserFollow> selectFollowingList(Long userId);

    /**
     * 查询粉丝列表
     *
     * @param userId 用户ID
     * @return 粉丝列表
     */
    List<UserFollow> selectFollowerList(Long userId);

    /**
     * 查询互相关注列表（好友）
     *
     * @param userId 用户ID
     * @return 好友列表
     */
    List<UserFollow> selectMutualFollowList(Long userId);

    /**
     * 检查是否已关注
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否已关注
     */
    boolean checkUserFollowed(Long followerId, Long followingId);

    /**
     * 检查是否互相关注
     *
     * @param userId1 用户1
     * @param userId2 用户2
     * @return 是否互关
     */
    boolean checkMutualFollow(Long userId1, Long userId2);

    /**
     * 关注用户
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 结果
     */
    int followUser(Long followerId, Long followingId);

    /**
     * 取消关注
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 结果
     */
    int unfollowUser(Long followerId, Long followingId);

    /**
     * 统计关注数
     *
     * @param userId 用户ID
     * @return 关注数
     */
    int countFollowing(Long userId);

    /**
     * 统计粉丝数
     *
     * @param userId 用户ID
     * @return 粉丝数
     */
    int countFollower(Long userId);

    /**
     * 批量检查是否已关注
     *
     * @param followerId 关注者ID
     * @param userIds 被查询的用户ID列表
     * @return Map<userId, Boolean>
     */
    Map<Long, Boolean> checkUserFollowedBatch(Long followerId, List<Long> userIds);
}