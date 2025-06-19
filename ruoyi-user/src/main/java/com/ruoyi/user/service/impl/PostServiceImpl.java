package com.ruoyi.user.service.impl;

import com.ruoyi.user.domain.Post;
import com.ruoyi.user.mapper.PostMapper;
import com.ruoyi.user.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 论坛帖子Service业务层处理
 *
 * @author SockLightDust
 * @date 2025-05-24
 */
@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostMapper postMapper;

    /**
     * 查询论坛帖子
     *
     * @param postId 论坛帖子主键
     * @return 论坛帖子
     */
    @Override
    public Post selectPostById(Integer postId) {
        return postMapper.selectPostById(postId);
    }

    /**
     * 查询论坛帖子详情（包含浏览数增加）
     *
     * @param postId 论坛帖子主键
     * @return 论坛帖子
     */
    @Override
    @Transactional
    public Post selectPostDetail(Integer postId) {
        // 先增加浏览数
        postMapper.increaseViewCount(postId);
        // 再查询帖子详情
        return postMapper.selectPostById(postId);
    }

    /**
     * 查询论坛帖子列表
     *
     * @param post 论坛帖子
     * @return 论坛帖子
     */
    @Override
    public List<Post> selectPostList(Post post) {
        return postMapper.selectPostList(post);
    }

    /**
     * 根据版块ID查询帖子列表
     *
     * @param sectionId 版块ID
     * @return 帖子列表
     */
    @Override
    public List<Post> selectPostListBySectionId(Integer sectionId) {
        return postMapper.selectPostListBySectionId(sectionId);
    }

    /**
     * 根据用户ID查询帖子列表
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    @Override
    public List<Post> selectPostListByUserId(Long userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    /**
     * 根据帖子标题搜索帖子
     *
     * @param postTitle 帖子标题关键词
     * @return 帖子列表
     */
    @Override
    public List<Post> selectPostListByTitle(String postTitle) {
        return postMapper.selectPostListByTitle(postTitle);
    }

    /**
     * 查询热门帖子列表
     *
     * @param limit 限制数量
     * @return 帖子列表
     */
    @Override
    public List<Post> selectHotPostList(Integer limit) {
        return postMapper.selectHotPostList(limit);
    }

    /**
     * 查询置顶帖子列表
     *
     * @param sectionId 版块ID（可选）
     * @return 帖子列表
     */
    @Override
    public List<Post> selectTopPostList(Integer sectionId) {
        return postMapper.selectTopPostList(sectionId);
    }

    /**
     * 新增论坛帖子
     *
     * @param post 论坛帖子
     * @return 结果
     */
    @Override
    public int insertPost(Post post) {
        post.setCreateTime(new Date());
        post.setStatus("0"); // 默认正常状态
        post.setDelFlag("0"); // 默认未删除
        post.setTopFlag("0"); // 默认不置顶
        post.setHotFlag("0"); // 默认不热门
        post.setLikeCount(0); // 初始点赞数为0
        post.setCommentCount(0); // 初始评论数为0
        post.setViewCount(0); // 初始浏览数为0
        return postMapper.insertPost(post);
    }

    /**
     * 修改论坛帖子
     *
     * @param post 论坛帖子
     * @return 结果
     */
    @Override
    public int updatePost(Post post) {
        post.setUpdateTime(new Date());
        return postMapper.updatePost(post);
    }

    /**
     * 删除论坛帖子（逻辑删除，用户端隐藏）
     *
     * @param postId 论坛帖子主键
     * @param updateBy 更新者
     * @return 结果
     */
    @Override
    public int hidePost(Integer postId, String updateBy) {
        return postMapper.deletePostById(postId, updateBy);
    }

    /**
     * 批量删除论坛帖子（逻辑删除，用户端隐藏）
     *
     * @param postIds 需要删除的数据主键集合
     * @param updateBy 更新者
     * @return 结果
     */
    @Override
    public int hidePosts(Integer[] postIds, String updateBy) {
        return postMapper.deletePostByIds(postIds, updateBy);
    }

    /**
     * 点赞帖子
     *
     * @param postId 帖子ID
     * @return 结果
     */
    @Override
    public int likePost(Integer postId) {
        return postMapper.updateLikeCount(postId, 1);
    }

    /**
     * 取消点赞帖子
     *
     * @param postId 帖子ID
     * @return 结果
     */
    @Override
    public int unlikePost(Integer postId) {
        return postMapper.updateLikeCount(postId, -1);
    }

    /**
     * 查询用户的帖子统计信息
     *
     * @param userId 用户ID
     * @return 统计信息
     */
    @Override
    public Post selectUserPostStats(Long userId) {
        return postMapper.selectUserPostStats(userId);
    }

    @Override
    public boolean hasUserLikedPost(Long userId, Integer postId) {
        return postMapper.hasUserLikedPost(userId, postId) > 0;
    }

    @Override
    @Transactional
    public int likePost(Long userId, Integer postId) {
        try {
            // 1. 先添加点赞记录
            int insertResult = postMapper.insertPostLike(userId, postId);
            if (insertResult <= 0) {
                throw new RuntimeException("添加点赞记录失败");
            }

            // 2. 更新帖子的点赞数
            int updateResult = postMapper.updateLikeCount(postId, 1);
            if (updateResult <= 0) {
                throw new RuntimeException("更新点赞数失败");
            }

            return 1;
        } catch (Exception e) {
            // 事务会自动回滚
            throw new RuntimeException("点赞操作失败", e);
        }
    }

    @Override
    @Transactional
    public int unlikePost(Long userId, Integer postId) {
        try {
            // 1. 先删除点赞记录
            int deleteResult = postMapper.deletePostLike(userId, postId);
            if (deleteResult <= 0) {
                throw new RuntimeException("删除点赞记录失败");
            }

            // 2. 更新帖子的点赞数
            int updateResult = postMapper.updateLikeCount(postId, -1);
            if (updateResult <= 0) {
                throw new RuntimeException("更新点赞数失败");
            }

            return 1;
        } catch (Exception e) {
            // 事务会自动回滚
            throw new RuntimeException("取消点赞操作失败", e);
        }
    }
}