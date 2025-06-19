package com.ruoyi.user.domain;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/28上午 9:47
 * @Author : SockLightDust
 */
public class Announcement {

    /** 标题 */
    private String titel;

    /** 内容 */
    private String content;

    /** 发布时间 */
    private LocalDateTime time;

    /** 图片路径 */
    private String photo;

    /** 发布者ID */
    private Integer issuerId;

    // 构造方法
    public Announcement() {}

    public Announcement(String titel, String content, LocalDateTime time, String photo, Integer issuerId) {
        this.titel = titel;
        this.content = content;
        this.time = time;
        this.photo = photo;
        this.issuerId = issuerId;
    }

    // Getter 和 Setter 方法
    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Integer issuerId) {
        this.issuerId = issuerId;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "titel='" + titel + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", photo='" + photo + '\'' +
                ", issuerId=" + issuerId +
                '}';
    }
}