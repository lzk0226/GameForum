package com.ruoyi.forum.mapper;

import com.ruoyi.forum.domain.BizCommentLike;

import java.util.List;

/**
 * 评论点赞Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public interface BizCommentLikeMapper 
{
    /**
     * 查询评论点赞
     * 
     * @param userId 评论点赞主键
     * @return 评论点赞
     */
    public BizCommentLike selectBizCommentLikeByUserId(Long userId);

    /**
     * 查询评论点赞列表
     * 
     * @param bizCommentLike 评论点赞
     * @return 评论点赞集合
     */
    public List<BizCommentLike> selectBizCommentLikeList(BizCommentLike bizCommentLike);

    /**
     * 新增评论点赞
     * 
     * @param bizCommentLike 评论点赞
     * @return 结果
     */
    public int insertBizCommentLike(BizCommentLike bizCommentLike);

    /**
     * 修改评论点赞
     * 
     * @param bizCommentLike 评论点赞
     * @return 结果
     */
    public int updateBizCommentLike(BizCommentLike bizCommentLike);

    /**
     * 删除评论点赞
     * 
     * @param userId 评论点赞主键
     * @return 结果
     */
    public int deleteBizCommentLikeByUserId(Long userId);

    /**
     * 批量删除评论点赞
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCommentLikeByUserIds(Long[] userIds);
}
