package com.ruoyi.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关注对象 biz_user_follow
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
public class UserFollow implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 关注者ID（粉丝ID） */
    @Excel(name = "关注者ID")
    private Long followerId;

    /** 被关注者ID（博主ID） */
    @Excel(name = "被关注者ID")
    private Long followingId;

    /** 关注时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "关注时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // 扩展字段（用于显示）
    /** 关注者昵称 */
    @Excel(name = "关注者昵称")
    private String followerNickName;

    /** 关注者头像 */
    private String followerAvatar;

    /** 被关注者昵称 */
    @Excel(name = "被关注者昵称")
    private String followingNickName;

    /** 被关注者头像 */
    private String followingAvatar;

    /** 是否互相关注 */
    private Boolean isMutual;

    /** 粉丝数（被关注者的粉丝数） */
    private Integer followerCount;

    /** 关注数（被关注者的关注数） */
    private Integer followingCount;

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFollowerNickName() {
        return followerNickName;
    }

    public void setFollowerNickName(String followerNickName) {
        this.followerNickName = followerNickName;
    }

    public String getFollowerAvatar() {
        return followerAvatar;
    }

    public void setFollowerAvatar(String followerAvatar) {
        this.followerAvatar = followerAvatar;
    }

    public String getFollowingNickName() {
        return followingNickName;
    }

    public void setFollowingNickName(String followingNickName) {
        this.followingNickName = followingNickName;
    }

    public String getFollowingAvatar() {
        return followingAvatar;
    }

    public void setFollowingAvatar(String followingAvatar) {
        this.followingAvatar = followingAvatar;
    }

    public Boolean getIsMutual() {
        return isMutual;
    }

    public void setIsMutual(Boolean isMutual) {
        this.isMutual = isMutual;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("followerId", getFollowerId())
                .append("followingId", getFollowingId())
                .append("createTime", getCreateTime())
                .append("followerNickName", getFollowerNickName())
                .append("followingNickName", getFollowingNickName())
                .toString();
    }
}