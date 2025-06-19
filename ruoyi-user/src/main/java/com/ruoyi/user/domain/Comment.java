package com.ruoyi.user.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 评论对象 biz_comment
 *
 * @author SockLightDust
 * @date 2025-05-27
 */
public class Comment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Integer commentId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 5000, message = "评论内容不能超过5000个字符")
    private String commentContent;

    /** 帖子ID */
    @Excel(name = "帖子ID")
    @NotNull(message = "帖子ID不能为空")
    private Integer postId;

    /** 对应游戏ID（可选） */
    @Excel(name = "对应游戏ID")
    private Integer gameId;

    /** 评论用户ID */
    @Excel(name = "评论用户ID")
    private Long userId;

    /** 父评论ID */
    @Excel(name = "父评论ID")
    private Integer parentId;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Integer likeCount;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    // 扩展字段（用于显示）
    /** 用户名 */
    @Excel(name = "用户名")
    private String nickName;

    /** 用户头像 */
    private String userAvatar;

    /** 帖子标题 */
    private String postTitle;

    /** 游戏名称 */
    private String gameName;

    /** 父评论内容 */
    private String parentContent;

    /** 父评论用户名 */
    private String parentUserName;

    /** 子评论列表 */
    private List<Comment> children;

    /** 子评论数量 */
    private Integer childrenCount;

    /** 是否已点赞 */
    private Boolean hasLiked;

    /** 用户统计信息 - 总评论数 */
    private Integer totalComments;

    /** 用户统计信息 - 总获赞数 */
    private Integer totalLikes;
    /** 父评论用户昵称 */
    private String parentNickName;

    public String getParentNickName() {
        return parentNickName;
    }

    public void setParentNickName(String parentNickName) {
        this.parentNickName = parentNickName;
    }


    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getParentContent() {
        return parentContent;
    }

    public void setParentContent(String parentContent) {
        this.parentContent = parentContent;
    }

    public String getParentUserName() {
        return parentUserName;
    }

    public void setParentUserName(String parentUserName) {
        this.parentUserName = parentUserName;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public Boolean getHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(Boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
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