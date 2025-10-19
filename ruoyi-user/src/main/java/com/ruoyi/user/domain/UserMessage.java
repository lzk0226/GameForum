package com.ruoyi.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户消息对象 biz_user_message
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
public class UserMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long messageId;

    /** 接收者用户ID */
    @Excel(name = "接收者ID")
    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;

    /** 发送者用户ID（系统消息时可为空） */
    @Excel(name = "发送者ID")
    private Long senderId;

    /** 消息类型（0点赞 1评论 2关注 3收藏 4系统消息） */
    @Excel(name = "消息类型", readConverterExp = "0=点赞,1=评论,2=关注,3=收藏,4=系统消息")
    @NotBlank(message = "消息类型不能为空")
    private String messageType;

    /** 消息内容 */
    @Excel(name = "消息内容")
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 500, message = "消息内容不能超过500个字符")
    private String messageContent;

    /** 关联类型（0帖子 1评论） */
    @Excel(name = "关联类型", readConverterExp = "0=帖子,1=评论")
    private String relatedType;

    /** 关联ID（帖子ID或评论ID） */
    @Excel(name = "关联ID")
    private Integer relatedId;

    /** 是否已读（0未读 1已读） */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    private String isRead;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 阅读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "阅读时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    // 扩展字段（用于显示）
    /** 接收者昵称 */
    @Excel(name = "接收者昵称")
    private String receiverNickName;

    /** 接收者头像 */
    private String receiverAvatar;

    /** 发送者昵称 */
    @Excel(name = "发送者昵称")
    private String senderNickName;

    /** 发送者头像 */
    private String senderAvatar;

    /** 关联帖子标题 */
    private String postTitle;

    /** 关联评论内容 */
    private String commentContent;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getReceiverNickName() {
        return receiverNickName;
    }

    public void setReceiverNickName(String receiverNickName) {
        this.receiverNickName = receiverNickName;
    }

    public String getReceiverAvatar() {
        return receiverAvatar;
    }

    public void setReceiverAvatar(String receiverAvatar) {
        this.receiverAvatar = receiverAvatar;
    }

    public String getSenderNickName() {
        return senderNickName;
    }

    public void setSenderNickName(String senderNickName) {
        this.senderNickName = senderNickName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
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