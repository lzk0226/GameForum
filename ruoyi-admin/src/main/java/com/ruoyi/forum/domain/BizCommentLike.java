package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评论点赞对象 biz_comment_like
 *
 * @author ruoyi
 * @date 2025-05-17
 */
public class BizCommentLike extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 评论ID */
    private Long commentId;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String commentContent;

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setCommentId(Long commentId)
    {
        this.commentId = commentId;
    }

    public Long getCommentId()
    {
        return commentId;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setCommentContent(String commentContent)
    {
        this.commentContent = commentContent;
    }

    public String getCommentContent()
    {
        return commentContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("commentId", getCommentId())
                .append("nickName", getNickName())
                .append("commentContent", getCommentContent())
                .append("createTime", getCreateTime())
                .toString();
    }
}