package com.ruoyi.forum.service;

import java.util.List;
import com.ruoyi.forum.domain.BizPostRanking;

/**
 * 帖子排名Service接口
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public interface IBizPostRankingService 
{
    /**
     * 查询帖子排名
     * 
     * @param postId 帖子排名主键
     * @return 帖子排名
     */
    public BizPostRanking selectBizPostRankingByPostId(Long postId);

    /**
     * 查询帖子排名列表
     * 
     * @param bizPostRanking 帖子排名
     * @return 帖子排名集合
     */
    public List<BizPostRanking> selectBizPostRankingList(BizPostRanking bizPostRanking);

    /**
     * 新增帖子排名
     * 
     * @param bizPostRanking 帖子排名
     * @return 结果
     */
    public int insertBizPostRanking(BizPostRanking bizPostRanking);

    /**
     * 修改帖子排名
     * 
     * @param bizPostRanking 帖子排名
     * @return 结果
     */
    public int updateBizPostRanking(BizPostRanking bizPostRanking);

    /**
     * 批量删除帖子排名
     * 
     * @param postIds 需要删除的帖子排名主键集合
     * @return 结果
     */
    public int deleteBizPostRankingByPostIds(Long[] postIds);

    /**
     * 删除帖子排名信息
     * 
     * @param postId 帖子排名主键
     * @return 结果
     */
    public int deleteBizPostRankingByPostId(Long postId);
}
