package com.ruoyi.user.service;

import com.ruoyi.user.domain.Post;

import java.util.List;

/**
 * 论坛帖子Service接口
 *
 * @author SockLightDust
 * @date 2025-05-24
 */
public interface IPostService {

    /**
     * 查询论坛帖子
     *
     * @param postId 论坛帖子主键
     * @return 论坛帖子
     */
    Post selectPostById(Integer postId);

    /**
     * 查询论坛帖子详情（包含浏览数增加）
     *
     * @param postId 论坛帖子主键
     * @return 论坛帖子
     */
    Post selectPostDetail(Integer postId);

    /**
     * 查询论坛帖子列表
     *
     * @param post 论坛帖子
     * @return 论坛帖子集合
     */
    List<Post> selectPostList(Post post);

    /**
     * 根据版块ID查询帖子列表
     *
     * @param sectionId 版块ID
     * @return 帖子列表
     */
    List<Post> selectPostListBySectionId(Integer sectionId);

    /**
     * 根据用户ID查询帖子列表
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    List<Post> selectPostListByUserId(Long userId);

    /**
     * 根据帖子标题搜索帖子
     *
     * @param postTitle 帖子标题关键词
     * @return 帖子列表
     */
    List<Post> selectPostListByTitle(String postTitle);

    /**
     * 查询热门帖子列表
     *
     * @param limit 限制数量
     * @return 帖子列表
     */
    List<Post> selectHotPostList(Integer limit);

    /**
     * 查询置顶帖子列表
     *
     * @param sectionId 版块ID（可选）
     * @return 帖子列表
     */
    List<Post> selectTopPostList(Integer sectionId);

    /**
     * 新增论坛帖子
     *
     * @param post 论坛帖子
     * @return 结果
     */
    int insertPost(Post post);

    /**
     * 修改论坛帖子
     *
     * @param post 论坛帖子
     * @return 结果
     */
    int updatePost(Post post);

    /**
     * 删除论坛帖子（逻辑删除，用户端隐藏）
     *
     * @param postId 论坛帖子主键
     * @param updateBy 更新者
     * @return 结果
     */
    int hidePost(Integer postId, String updateBy);

    /**
     * 批量删除论坛帖子（逻辑删除，用户端隐藏）
     *
     * @param postIds 需要删除的数据主键集合
     * @param updateBy 更新者
     * @return 结果
     */
    int hidePosts(Integer[] postIds, String updateBy);

    /**
     * 点赞帖子
     *
     * @param postId 帖子ID
     * @return 结果
     */
    int likePost(Integer postId);

    /**
     * 取消点赞帖子
     *
     * @param postId 帖子ID
     * @return 结果
     */
    int unlikePost(Integer postId);

    /**
     * 查询用户的帖子统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     */
    Post selectUserPostStats(Long userId);
    /**
     * 检查用户是否已经点赞过某个帖子
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已点赞
     */
    boolean hasUserLikedPost(Long userId, Integer postId);

    /**
     * 点赞帖子（添加点赞记录并增加点赞数）
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    int likePost(Long userId, Integer postId);

    /**
     * 取消点赞帖子（删除点赞记录并减少点赞数）
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    int unlikePost(Long userId, Integer postId);
}