package com.ruoyi.forum.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户消息对象 biz_user_message
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class BizUserMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long messageId;

    /** 接收者用户ID */
    @Excel(name = "接收者用户ID")
    private Long receiverId;

    /** 发送者用户ID（系统消息时可为空） */
    @Excel(name = "发送者用户ID", readConverterExp = "系=统消息时可为空")
    private Long senderId;

    /** 消息类型（0点赞 1评论 2关注 3收藏 4系统消息） */
    @Excel(name = "消息类型", readConverterExp = "0=点赞,1=评论,2=关注,3=收藏,4=系统消息")
    private String messageType;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String messageContent;

    /** 关联类型（0帖子 1评论） */
    @Excel(name = "关联类型", readConverterExp = "0=帖子,1=评论")
    private String relatedType;

    /** 关联ID（帖子ID或评论ID） */
    @Excel(name = "关联ID", readConverterExp = "帖=子ID或评论ID")
    private Long relatedId;

    /** 是否已读（0未读 1已读） */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    private String isRead;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 阅读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "阅读时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date readTime;

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }

    public void setReceiverId(Long receiverId) 
    {
        this.receiverId = receiverId;
    }

    public Long getReceiverId() 
    {
        return receiverId;
    }

    public void setSenderId(Long senderId) 
    {
        this.senderId = senderId;
    }

    public Long getSenderId() 
    {
        return senderId;
    }

    public void setMessageType(String messageType) 
    {
        this.messageType = messageType;
    }

    public String getMessageType() 
    {
        return messageType;
    }

    public void setMessageContent(String messageContent) 
    {
        this.messageContent = messageContent;
    }

    public String getMessageContent() 
    {
        return messageContent;
    }

    public void setRelatedType(String relatedType) 
    {
        this.relatedType = relatedType;
    }

    public String getRelatedType() 
    {
        return relatedType;
    }

    public void setRelatedId(Long relatedId) 
    {
        this.relatedId = relatedId;
    }

    public Long getRelatedId() 
    {
        return relatedId;
    }

    public void setIsRead(String isRead) 
    {
        this.isRead = isRead;
    }

    public String getIsRead() 
    {
        return isRead;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public void setReadTime(Date readTime) 
    {
        this.readTime = readTime;
    }

    public Date getReadTime() 
    {
        return readTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("messageId", getMessageId())
            .append("receiverId", getReceiverId())
            .append("senderId", getSenderId())
            .append("messageType", getMessageType())
            .append("messageContent", getMessageContent())
            .append("relatedType", getRelatedType())
            .append("relatedId", getRelatedId())
            .append("isRead", getIsRead())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("readTime", getReadTime())
            .toString();
    }
}
