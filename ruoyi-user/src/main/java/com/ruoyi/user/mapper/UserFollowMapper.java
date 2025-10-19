package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.UserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 用户关注关系Mapper接口
 * 对应XML：UserFollowMapper.xml
 */
@Mapper
public interface UserFollowMapper {

    /**
     * 查询用户关注列表
     * @param userId 用户ID
     * @return 关注列表
     */
    List<UserFollow> selectFollowingListByUserId(@Param("userId") Long userId);

    /**
     * 查询用户粉丝列表
     * @param userId 用户ID
     * @return 粉丝列表
     */
    List<UserFollow> selectFollowerListByUserId(@Param("userId") Long userId);

    /**
     * 检查是否已关注
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否已关注（0 或 1）
     */
    int checkUserFollowed(@Param("followerId") Long followerId,
                          @Param("followingId") Long followingId);

    /**
     * 检查是否互相关注
     * @param userId1 用户1
     * @param userId2 用户2
     * @return 是否互相关注
     */
    boolean checkMutualFollow(@Param("userId1") Long userId1,
                              @Param("userId2") Long userId2);

    /**
     * 添加关注
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 插入结果
     */
    int insertFollow(@Param("followerId") Long followerId,
                     @Param("followingId") Long followingId);

    /**
     * 取消关注
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 删除结果
     */
    int deleteFollow(@Param("followerId") Long followerId,
                     @Param("followingId") Long followingId);

    /**
     * 获取用户关注数
     * @param userId 用户ID
     * @return 关注数量
     */
    int countFollowingByUserId(@Param("userId") Long userId);

    /**
     * 获取用户粉丝数
     * @param userId 用户ID
     * @return 粉丝数量
     */
    int countFollowerByUserId(@Param("userId") Long userId);

    /**
     * 批量查询是否已关注（返回 user_id → followed）
     * @param followerId 当前登录用户ID
     * @param userIds 被查询的用户ID列表
     * @return 已关注的用户映射
     */
    List<HashMap<String, Object>> checkUserFollowedBatch(@Param("followerId") Long followerId,
                                                         @Param("userIds") List<Long> userIds);

    /**
     * 获取互相关注（好友）列表
     * @param userId 用户ID
     * @return 互相关注列表
     */
    List<UserFollow> selectMutualFollowList(@Param("userId") Long userId);
}