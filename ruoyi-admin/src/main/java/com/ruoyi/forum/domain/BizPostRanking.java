package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 帖子排名对象 biz_post_ranking
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public class BizPostRanking extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 帖子ID */
    private Long postId;

    /** 日排行 */
    @Excel(name = "日排行")
    private Long dayRank;

    /** 周排行 */
    @Excel(name = "周排行")
    private Long weekRank;

    /** 月排行 */
    @Excel(name = "月排行")
    private Long monthRank;

    /** 年排行 */
    @Excel(name = "年排行")
    private Long yearRank;

    /** 总排行 */
    @Excel(name = "总排行")
    private Long totalRank;

    public void setPostId(Long postId) 
    {
        this.postId = postId;
    }

    public Long getPostId() 
    {
        return postId;
    }

    public void setDayRank(Long dayRank) 
    {
        this.dayRank = dayRank;
    }

    public Long getDayRank() 
    {
        return dayRank;
    }

    public void setWeekRank(Long weekRank) 
    {
        this.weekRank = weekRank;
    }

    public Long getWeekRank() 
    {
        return weekRank;
    }

    public void setMonthRank(Long monthRank) 
    {
        this.monthRank = monthRank;
    }

    public Long getMonthRank() 
    {
        return monthRank;
    }

    public void setYearRank(Long yearRank) 
    {
        this.yearRank = yearRank;
    }

    public Long getYearRank() 
    {
        return yearRank;
    }

    public void setTotalRank(Long totalRank) 
    {
        this.totalRank = totalRank;
    }

    public Long getTotalRank() 
    {
        return totalRank;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("postId", getPostId())
            .append("dayRank", getDayRank())
            .append("weekRank", getWeekRank())
            .append("monthRank", getMonthRank())
            .append("yearRank", getYearRank())
            .append("totalRank", getTotalRank())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
