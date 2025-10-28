package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 帖子收藏对象 biz_post_favorite
 *
 * @author ruoyi
 * @date 2025-10-20
 */
public class BizPostFavorite extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 帖子ID */
    private Long postId;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 帖子标题 */
    @Excel(name = "帖子标题")
    private String postTitle;

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    public Long getPostId()
    {
        return postId;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setPostTitle(String postTitle)
    {
        this.postTitle = postTitle;
    }

    public String getPostTitle()
    {
        return postTitle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("postId", getPostId())
                .append("nickName", getNickName())
                .append("postTitle", getPostTitle())
                .append("createTime", getCreateTime())
                .toString();
    }
}