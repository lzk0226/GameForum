package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户关注对象 biz_user_follow
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class BizUserFollow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关注者ID（粉丝ID） */
    private Long followerId;

    /** 被关注者ID（博主ID） */
    private Long followingId;

    public void setFollowerId(Long followerId) 
    {
        this.followerId = followerId;
    }

    public Long getFollowerId() 
    {
        return followerId;
    }

    public void setFollowingId(Long followingId) 
    {
        this.followingId = followingId;
    }

    public Long getFollowingId() 
    {
        return followingId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("followerId", getFollowerId())
            .append("followingId", getFollowingId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
