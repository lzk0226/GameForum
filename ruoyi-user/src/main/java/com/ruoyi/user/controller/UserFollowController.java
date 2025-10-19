package com.ruoyi.user.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.R.R;
import com.ruoyi.user.domain.User;
import com.ruoyi.user.domain.UserFollow;
import com.ruoyi.user.emun.ResultCodeEnum;
import com.ruoyi.user.service.IUserFollowService;
import com.ruoyi.user.service.IUserService;
import com.ruoyi.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 用户关注 Controller
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
@RestController
@RequestMapping("/user/follow")
@Validated
public class UserFollowController {

    @Autowired
    private IUserFollowService userFollowService;

    @Autowired
    private IUserService userService;

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
     * 获取当前用户的关注列表
     */
    @GetMapping("/following/my")
    public R<?> getMyFollowingList(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        List<UserFollow> followingList = userFollowService.selectFollowingList(userId);
        return R.ok(followingList);
    }

    /**
     * 获取指定用户的关注列表
     */
    @GetMapping("/following/{userId}")
    public R<?> getUserFollowingList(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        // 检查用户是否存在
        User user = userService.selectUserById(userId);
        if (user == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        List<UserFollow> followingList = userFollowService.selectFollowingList(userId);
        return R.ok(followingList);
    }

    /**
     * 获取当前用户的粉丝列表
     */
    @GetMapping("/follower/my")
    public R<?> getMyFollowerList(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        List<UserFollow> followerList = userFollowService.selectFollowerList(userId);
        return R.ok(followerList);
    }

    /**
     * 获取指定用户的粉丝列表
     */
    @GetMapping("/follower/{userId}")
    public R<?> getUserFollowerList(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        // 检查用户是否存在
        User user = userService.selectUserById(userId);
        if (user == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        List<UserFollow> followerList = userFollowService.selectFollowerList(userId);
        return R.ok(followerList);
    }

    /**
     * 获取当前用户的互相关注列表（好友列表）
     */
    @GetMapping("/mutual/my")
    public R<?> getMyMutualFollowList(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        List<UserFollow> mutualFollowList = userFollowService.selectMutualFollowList(userId);
        return R.ok(mutualFollowList);
    }

    /**
     * 获取指定用户的互相关注列表
     */
    @GetMapping("/mutual/{userId}")
    public R<?> getUserMutualFollowList(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        // 检查用户是否存在
        User user = userService.selectUserById(userId);
        if (user == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        List<UserFollow> mutualFollowList = userFollowService.selectMutualFollowList(userId);
        return R.ok(mutualFollowList);
    }

    /**
     * 关注用户
     */
    @PostMapping("/{userId}")
    public R<?> followUser(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long followingId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long followerId = validateTokenAndGetUserId(authHeader);
        if (followerId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 不能关注自己
        if (followerId.equals(followingId)) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "不能关注自己");
        }

        // 检查被关注的用户是否存在
        User followingUser = userService.selectUserById(followingId);
        if (followingUser == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        // 检查用户状态
        if (!"0".equals(followingUser.getStatus())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "该用户已被禁用");
        }
        if ("2".equals(followingUser.getDelFlag())) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "该用户不存在");
        }

        // 检查是否已关注
        if (userFollowService.checkUserFollowed(followerId, followingId)) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "您已经关注过该用户");
        }

        // 关注用户
        int result = userFollowService.followUser(followerId, followingId);
        return result > 0 ? R.ok("关注成功") : R.fail("关注失败");
    }

    /**
     * 取消关注用户
     */
    @DeleteMapping("/{userId}")
    public R<?> unfollowUser(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long followingId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long followerId = validateTokenAndGetUserId(authHeader);
        if (followerId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 检查被关注的用户是否存在
        User followingUser = userService.selectUserById(followingId);
        if (followingUser == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        // 检查是否已关注
        if (!userFollowService.checkUserFollowed(followerId, followingId)) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "您还没有关注该用户");
        }

        // 取消关注
        int result = userFollowService.unfollowUser(followerId, followingId);
        return result > 0 ? R.ok("取消关注成功") : R.fail("取消关注失败");
    }

    /**
     * 检查当前用户是否关注了某个用户
     */
    @GetMapping("/check/{userId}")
    public R<?> checkFollowStatus(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long followingId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long followerId = validateTokenAndGetUserId(authHeader);
        if (followerId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 检查用户是否存在
        User followingUser = userService.selectUserById(followingId);
        if (followingUser == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        boolean isFollowed = userFollowService.checkUserFollowed(followerId, followingId);
        return R.ok(isFollowed);
    }

    /**
     * 批量检查当前用户是否关注了指定用户
     */
    @PostMapping("/check/batch")
    public R<?> checkFollowStatusBatch(
            @RequestBody List<Long> userIds,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long followerId = validateTokenAndGetUserId(authHeader);
        if (followerId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        if (userIds == null || userIds.isEmpty()) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户ID列表不能为空");
        }

        Map<Long, Boolean> followStatus = userFollowService.checkUserFollowedBatch(followerId, userIds);
        return R.ok(followStatus);
    }

    /**
     * 检查两个用户是否互相关注
     */
    @GetMapping("/check/mutual/{userId}")
    public R<?> checkMutualFollowStatus(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId2,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId1 = validateTokenAndGetUserId(authHeader);
        if (userId1 == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        // 检查用户是否存在
        User user = userService.selectUserById(userId2);
        if (user == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        boolean isMutual = userFollowService.checkMutualFollow(userId1, userId2);
        return R.ok(isMutual);
    }

    /**
     * 获取当前用户的关注数
     */
    @GetMapping("/count/following/my")
    public R<?> getMyFollowingCount(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        int count = userFollowService.countFollowing(userId);
        return R.ok(count);
    }

    /**
     * 获取指定用户的关注数
     */
    @GetMapping("/count/following/{userId}")
    public R<?> getUserFollowingCount(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        // 检查用户是否存在
        User user = userService.selectUserById(userId);
        if (user == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        int count = userFollowService.countFollowing(userId);
        return R.ok(count);
    }

    /**
     * 获取当前用户的粉丝数
     */
    @GetMapping("/count/follower/my")
    public R<?> getMyFollowerCount(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = validateTokenAndGetUserId(authHeader);
        if (userId == null) {
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        int count = userFollowService.countFollower(userId);
        return R.ok(count);
    }

    /**
     * 获取指定用户的粉丝数
     */
    @GetMapping("/count/follower/{userId}")
    public R<?> getUserFollowerCount(
            @PathVariable("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        // 检查用户是否存在
        User user = userService.selectUserById(userId);
        if (user == null) {
            return R.fail(ResultCodeEnum.VALIDATION_ERROR, "用户不存在");
        }

        int count = userFollowService.countFollower(userId);
        return R.ok(count);
    }
}