package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizPostMapper;
import com.ruoyi.forum.domain.BizPost;
import com.ruoyi.forum.service.IBizPostService;

/**
 * 论坛帖子Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizPostServiceImpl implements IBizPostService 
{
    @Autowired
    private BizPostMapper bizPostMapper;

    /**
     * 查询论坛帖子
     * 
     * @param postId 论坛帖子主键
     * @return 论坛帖子
     */
    @Override
    public BizPost selectBizPostByPostId(Long postId)
    {
        return bizPostMapper.selectBizPostByPostId(postId);
    }

    /**
     * 查询论坛帖子列表
     * 
     * @param bizPost 论坛帖子
     * @return 论坛帖子
     */
    @Override
    public List<BizPost> selectBizPostList(BizPost bizPost)
    {
        return bizPostMapper.selectBizPostList(bizPost);
    }

    /**
     * 新增论坛帖子
     * 
     * @param bizPost 论坛帖子
     * @return 结果
     */
    @Override
    public int insertBizPost(BizPost bizPost)
    {
        bizPost.setCreateTime(DateUtils.getNowDate());
        return bizPostMapper.insertBizPost(bizPost);
    }

    /**
     * 修改论坛帖子
     * 
     * @param bizPost 论坛帖子
     * @return 结果
     */
    @Override
    public int updateBizPost(BizPost bizPost)
    {
        bizPost.setUpdateTime(DateUtils.getNowDate());
        return bizPostMapper.updateBizPost(bizPost);
    }

    /**
     * 批量删除论坛帖子
     * 
     * @param postIds 需要删除的论坛帖子主键
     * @return 结果
     */
    @Override
    public int deleteBizPostByPostIds(Long[] postIds)
    {
        return bizPostMapper.deleteBizPostByPostIds(postIds);
    }

    /**
     * 删除论坛帖子信息
     * 
     * @param postId 论坛帖子主键
     * @return 结果
     */
    @Override
    public int deleteBizPostByPostId(Long postId)
    {
        return bizPostMapper.deleteBizPostByPostId(postId);
    }
}
