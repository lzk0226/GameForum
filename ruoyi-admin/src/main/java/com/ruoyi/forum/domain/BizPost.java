package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 论坛帖子对象 biz_post
 *
 * @author ruoyi
 * @date 2025-05-17
 */
public class BizPost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 帖子ID */
    private Long postId;

    /** 帖子标题 */
    @Excel(name = "帖子标题")
    private String postTitle;

    /** 帖子内容 */
    @Excel(name = "帖子内容")
    private String postContent;

    /** 发帖用户ID */
    @Excel(name = "发帖用户ID")
    private Long userId;

    /** 发帖用户昵称 */
    @Excel(name = "发帖用户昵称")
    private String nickName;

    /** 所属版块ID */
    @Excel(name = "所属版块ID")
    private Long sectionId;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likeCount;

    /** 评论数 */
    @Excel(name = "评论数")
    private Long commentCount;

    /** 浏览数 */
    @Excel(name = "浏览数")
    private Long viewCount;

    /** 置顶标志（0不置顶 1置顶） */
    @Excel(name = "置顶标志", readConverterExp = "0=不置顶,1=置顶")
    private String topFlag;

    /** 热门标志（0不热门 1热门） */
    @Excel(name = "热门标志", readConverterExp = "0=不热门,1=热门")
    private String hotFlag;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    public Long getPostId()
    {
        return postId;
    }

    public void setPostTitle(String postTitle)
    {
        this.postTitle = postTitle;
    }

    public String getPostTitle()
    {
        return postTitle;
    }

    public void setPostContent(String postContent)
    {
        this.postContent = postContent;
    }

    public String getPostContent()
    {
        return postContent;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setSectionId(Long sectionId)
    {
        this.sectionId = sectionId;
    }

    public Long getSectionId()
    {
        return sectionId;
    }

    public void setLikeCount(Long likeCount)
    {
        this.likeCount = likeCount;
    }

    public Long getLikeCount()
    {
        return likeCount;
    }

    public void setCommentCount(Long commentCount)
    {
        this.commentCount = commentCount;
    }

    public Long getCommentCount()
    {
        return commentCount;
    }

    public void setViewCount(Long viewCount)
    {
        this.viewCount = viewCount;
    }

    public Long getViewCount()
    {
        return viewCount;
    }

    public void setTopFlag(String topFlag)
    {
        this.topFlag = topFlag;
    }

    public String getTopFlag()
    {
        return topFlag;
    }

    public void setHotFlag(String hotFlag)
    {
        this.hotFlag = hotFlag;
    }

    public String getHotFlag()
    {
        return hotFlag;
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
                .append("postId", getPostId())
                .append("postTitle", getPostTitle())
                .append("postContent", getPostContent())
                .append("userId", getUserId())
                .append("nickName", getNickName())
                .append("sectionId", getSectionId())
                .append("likeCount", getLikeCount())
                .append("commentCount", getCommentCount())
                .append("viewCount", getViewCount())
                .append("topFlag", getTopFlag())
                .append("hotFlag", getHotFlag())
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