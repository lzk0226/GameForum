package com.ruoyi.user.service;

import com.ruoyi.user.domain.PostFavorite;

import java.util.List;
import java.util.Map;

/**
 * 帖子收藏Service接口
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
public interface IPostFavoriteService {

    /**
     * 查询用户收藏列表
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<PostFavorite> selectFavoriteListByUserId(Long userId);

    /**
     * 查询帖子被收藏列表
     *
     * @param postId 帖子ID
     * @return 收藏用户列表
     */
    List<PostFavorite> selectFavoriteListByPostId(Integer postId);

    /**
     * 检查用户是否已收藏帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已收藏
     */
    boolean checkUserFavorited(Long userId, Integer postId);

    /**
     * 收藏帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 结果
     */
    int addFavorite(Long userId, Integer postId);

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 结果
     */
    int removeFavorite(Long userId, Integer postId);

    /**
     * 批量取消收藏
     *
     * @param userId 用户ID
     * @param postIds 帖子ID列表
     * @return 结果
     */
    int removeFavoriteByIds(Long userId, List<Integer> postIds);

    /**
     * 统计用户收藏数
     *
     * @param userId 用户ID
     * @return 收藏数
     */
    int countFavoriteByUserId(Long userId);

    /**
     * 统计帖子被收藏数
     *
     * @param postId 帖子ID
     * @return 被收藏数
     */
    int countFavoriteByPostId(Integer postId);

    /**
     * 批量检查用户是否收藏了指定帖子
     *
     * @param userId 用户ID
     * @param postIds 帖子ID列表
     * @return Map<postId, Boolean>
     */
    Map<Integer, Boolean> checkUserFavoritedBatch(Long userId, List<Integer> postIds);
}
