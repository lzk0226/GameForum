package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizPostRankingMapper;
import com.ruoyi.forum.domain.BizPostRanking;
import com.ruoyi.forum.service.IBizPostRankingService;

/**
 * 帖子排名Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizPostRankingServiceImpl implements IBizPostRankingService 
{
    @Autowired
    private BizPostRankingMapper bizPostRankingMapper;

    /**
     * 查询帖子排名
     * 
     * @param postId 帖子排名主键
     * @return 帖子排名
     */
    @Override
    public BizPostRanking selectBizPostRankingByPostId(Long postId)
    {
        return bizPostRankingMapper.selectBizPostRankingByPostId(postId);
    }

    /**
     * 查询帖子排名列表
     * 
     * @param bizPostRanking 帖子排名
     * @return 帖子排名
     */
    @Override
    public List<BizPostRanking> selectBizPostRankingList(BizPostRanking bizPostRanking)
    {
        return bizPostRankingMapper.selectBizPostRankingList(bizPostRanking);
    }

    /**
     * 新增帖子排名
     * 
     * @param bizPostRanking 帖子排名
     * @return 结果
     */
    @Override
    public int insertBizPostRanking(BizPostRanking bizPostRanking)
    {
        return bizPostRankingMapper.insertBizPostRanking(bizPostRanking);
    }

    /**
     * 修改帖子排名
     * 
     * @param bizPostRanking 帖子排名
     * @return 结果
     */
    @Override
    public int updateBizPostRanking(BizPostRanking bizPostRanking)
    {
        bizPostRanking.setUpdateTime(DateUtils.getNowDate());
        return bizPostRankingMapper.updateBizPostRanking(bizPostRanking);
    }

    /**
     * 批量删除帖子排名
     * 
     * @param postIds 需要删除的帖子排名主键
     * @return 结果
     */
    @Override
    public int deleteBizPostRankingByPostIds(Long[] postIds)
    {
        return bizPostRankingMapper.deleteBizPostRankingByPostIds(postIds);
    }

    /**
     * 删除帖子排名信息
     * 
     * @param postId 帖子排名主键
     * @return 结果
     */
    @Override
    public int deleteBizPostRankingByPostId(Long postId)
    {
        return bizPostRankingMapper.deleteBizPostRankingByPostId(postId);
    }
}
