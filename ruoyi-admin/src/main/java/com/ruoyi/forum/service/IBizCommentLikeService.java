package com.ruoyi.forum.service;

import java.util.List;
import com.ruoyi.forum.domain.BizCommentLike;

/**
 * 评论点赞Service接口
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public interface IBizCommentLikeService 
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
     * 批量删除评论点赞
     * 
     * @param userIds 需要删除的评论点赞主键集合
     * @return 结果
     */
    public int deleteBizCommentLikeByUserIds(Long[] userIds);

    /**
     * 删除评论点赞信息
     * 
     * @param userId 评论点赞主键
     * @return 结果
     */
    public int deleteBizCommentLikeByUserId(Long userId);
}
