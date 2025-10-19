package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.PostFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 用户帖子收藏 Mapper
 *
 * 对应 XML：PostFavoriteMapper.xml
 * @author SockLightDust
 */
@Mapper
public interface PostFavoriteMapper {

    /**
     * 查询用户收藏的帖子列表
     *
     * @param userId 用户ID
     * @return 收藏帖子列表
     */
    List<PostFavorite> selectFavoriteListByUserId(@Param("userId") Long userId);

    /**
     * 查询帖子被哪些用户收藏
     *
     * @param postId 帖子ID
     * @return 收藏该帖子的用户列表
     */
    List<PostFavorite> selectFavoriteListByPostId(@Param("postId") Integer postId);

    /**
     * 检查用户是否收藏了某个帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 1 表示已收藏，0 表示未收藏
     */
    int checkUserFavorited(@Param("userId") Long userId, @Param("postId") Integer postId);

    /**
     * 添加收藏记录
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    int insertFavorite(@Param("userId") Long userId, @Param("postId") Integer postId);

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    int deleteFavorite(@Param("userId") Long userId, @Param("postId") Integer postId);

    /**
     * 批量取消收藏
     *
     * @param userId  用户ID
     * @param postIds 帖子ID集合
     * @return 影响行数
     */
    int deleteFavoriteByIds(@Param("userId") Long userId, @Param("postIds") List<Integer> postIds);

    /**
     * 获取用户收藏的帖子总数
     *
     * @param userId 用户ID
     * @return 收藏数
     */
    int countFavoriteByUserId(@Param("userId") Long userId);

    /**
     * 获取帖子被收藏的总数
     *
     * @param postId 帖子ID
     * @return 收藏数
     */
    int countFavoriteByPostId(@Param("postId") Integer postId);

    /**
     * 批量检查用户是否收藏了指定帖子
     *
     * @param userId  用户ID
     * @param postIds 帖子ID集合
     * @return 帖子收藏状态 Map，key=post_id, value=1
     */
    List<HashMap<String, Object>> checkUserFavoritedBatch(@Param("userId") Long userId,
                                                          @Param("postIds") List<Integer> postIds);
}