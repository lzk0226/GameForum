package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评论对象 biz_comment
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public class BizComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Long commentId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String commentContent;

    /** 帖子ID */
    @Excel(name = "帖子ID")
    private Long postId;

    /** 对应游戏ID（可选） */
    @Excel(name = "对应游戏ID", readConverterExp = "可=选")
    private Long gameId;

    /** 评论用户ID */
    @Excel(name = "评论用户ID")
    private Long userId;

    /** 父评论ID */
    @Excel(name = "父评论ID")
    private Long parentId;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likeCount;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setCommentId(Long commentId) 
    {
        this.commentId = commentId;
    }

    public Long getCommentId() 
    {
        return commentId;
    }

    public void setCommentContent(String commentContent) 
    {
        this.commentContent = commentContent;
    }

    public String getCommentContent() 
    {
        return commentContent;
    }

    public void setPostId(Long postId) 
    {
        this.postId = postId;
    }

    public Long getPostId() 
    {
        return postId;
    }

    public void setGameId(Long gameId) 
    {
        this.gameId = gameId;
    }

    public Long getGameId() 
    {
        return gameId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setLikeCount(Long likeCount) 
    {
        this.likeCount = likeCount;
    }

    public Long getLikeCount() 
    {
        return likeCount;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("commentId", getCommentId())
            .append("commentContent", getCommentContent())
            .append("postId", getPostId())
            .append("gameId", getGameId())
            .append("userId", getUserId())
            .append("parentId", getParentId())
            .append("likeCount", getLikeCount())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
