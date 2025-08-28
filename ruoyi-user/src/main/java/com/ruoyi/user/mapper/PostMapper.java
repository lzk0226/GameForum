package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 论坛帖子Mapper接口
 *
 * @author SockLightDust
 * @date 2025-05-24
 */
@Mapper
public interface PostMapper {

    /**
     * 查询论坛帖子
     *
     * @param postId 论坛帖子主键
     * @return 论坛帖子
     */
    Post selectPostById(Integer postId);

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
    List<Post> selectPostListBySectionId(@Param("sectionId") Integer sectionId);

    /**
     * 根据用户ID查询帖子列表
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    List<Post> selectPostListByUserId(@Param("userId") Long userId);

    /**
     * 根据帖子标题搜索帖子
     *
     * @param postTitle 帖子标题关键词
     * @return 帖子列表
     */
    List<Post> selectPostListByTitle(@Param("postTitle") String postTitle);

    /**
     * 查询热门帖子列表
     *
     * @param limit 限制数量
     * @return 帖子列表
     */
    List<Post> selectHotPostList(@Param("limit") Integer limit);

    /**
     * 查询置顶帖子列表
     *
     * @param sectionId 版块ID（可选）
     * @return 帖子列表
     */
    List<Post> selectTopPostList(@Param("sectionId") Integer sectionId);

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
     * 删除论坛帖子（逻辑删除）
     *
     * @param postId 论坛帖子主键
     * @param updateBy 更新者
     * @return 结果
     */
    int deletePostById(@Param("postId") Integer postId, @Param("updateBy") String updateBy);

    /**
     * 批量删除论坛帖子（逻辑删除）
     *
     * @param postIds 需要删除的数据主键集合
     * @param updateBy 更新者
     * @return 结果
     */
    int deletePostByIds(@Param("postIds") Integer[] postIds, @Param("updateBy") String updateBy);

    /**
     * 增加帖子浏览数
     *
     * @param postId 帖子ID
     * @return 结果
     */
    int increaseViewCount(@Param("postId") Integer postId);

    /**
     * 更新帖子点赞数
     *
     * @param postId 帖子ID
     * @param increment 增量（可为负数）
     * @return 结果
     */
    int updateLikeCount(@Param("postId") Integer postId, @Param("increment") Integer increment);

    /**
     * 更新帖子评论数
     *
     * @param postId 帖子ID
     * @param increment 增量（可为负数）
     * @return 结果
     */
    int updateCommentCount(@Param("postId") Integer postId, @Param("increment") Integer increment);

    /**
     * 查询用户的帖子统计信息
     *
     * @param userId 用户ID
     * @return 统计信息（包含总数、点赞数、浏览数等）
     */
    Post selectUserPostStats(@Param("userId") Long userId);
    /**
     * 检查用户是否已经点赞过某个帖子
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 点赞记录数量
     */
    int hasUserLikedPost(@Param("userId") Long userId, @Param("postId") Integer postId);

    /**
     * 添加点赞记录
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    int insertPostLike(@Param("userId") Long userId, @Param("postId") Integer postId);

    /**
     * 删除点赞记录
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    int deletePostLike(@Param("userId") Long userId, @Param("postId") Integer postId);


    // 在PostMapper.java接口中添加以下方法：
    /**
     * 获取所有帖子点赞记录
     * @return 点赞记录列表
     */
    List<Map<String, Object>> selectAllPostLikes();

    /**
     * 获取所有评论记录
     * @return 评论记录列表
     */
    List<Map<String, Object>> selectAllComments();

    /**
     * 获取所有帖子记录
     * @return 帖子列表
     */
    List<Post> selectAllPosts();

    /**
     * 获取用户偏好的版块
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 版块ID列表
     */
    List<Integer> selectUserPreferredSections(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 获取指定版块的热门帖子
     * @param sectionId 版块ID
     * @param limit 限制数量
     * @return 帖子列表
     */
    List<Post> selectHotPostsBySectionId(@Param("sectionId") Integer sectionId, @Param("limit") Integer limit);

    /**
     * 检查用户是否与帖子有过交互
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return true表示有交互，false表示无交互
     */
    Boolean hasUserInteracted(@Param("userId") Long userId, @Param("postId") Integer postId);

    /**
     * 批量获取帖子详情
     * @param postIds 帖子ID列表
     * @return 帖子列表
     */
    List<Post> selectPostsByIds(@Param("postIds") List<Integer> postIds);

    /**
     * 根据版块ID查询最近热门帖子
     */
    List<Post> selectRecentHotPostsBySectionId(@Param("sectionId") Integer sectionId,
                                               @Param("limit") Integer limit);

    /**
     * 查询帖子详细信息（会增加浏览数）
     */
    Post selectPostDetail(Integer postId);

}