package com.ruoyi.forum.mapper;

import java.util.List;
import com.ruoyi.forum.domain.BizPost;

/**
 * 论坛帖子Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public interface BizPostMapper 
{
    /**
     * 查询论坛帖子
     * 
     * @param postId 论坛帖子主键
     * @return 论坛帖子
     */
    public BizPost selectBizPostByPostId(Long postId);

    /**
     * 查询论坛帖子列表
     * 
     * @param bizPost 论坛帖子
     * @return 论坛帖子集合
     */
    public List<BizPost> selectBizPostList(BizPost bizPost);

    /**
     * 新增论坛帖子
     * 
     * @param bizPost 论坛帖子
     * @return 结果
     */
    public int insertBizPost(BizPost bizPost);

    /**
     * 修改论坛帖子
     * 
     * @param bizPost 论坛帖子
     * @return 结果
     */
    public int updateBizPost(BizPost bizPost);

    /**
     * 删除论坛帖子
     * 
     * @param postId 论坛帖子主键
     * @return 结果
     */
    public int deleteBizPostByPostId(Long postId);

    /**
     * 批量删除论坛帖子
     * 
     * @param postIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizPostByPostIds(Long[] postIds);
}
