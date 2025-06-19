package com.ruoyi.forum.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 公告栏对象 biz_announcement
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public class BizAnnouncement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标题 */
    @Excel(name = "标题")
    private String titel;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

    /** 图片链接 */
    @Excel(name = "图片链接")
    private String photo;

    /** 发布人 */
    @Excel(name = "发布人")
    private Long issuerId;

    public void setTitel(String titel) 
    {
        this.titel = titel;
    }

    public String getTitel() 
    {
        return titel;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setTime(Date time) 
    {
        this.time = time;
    }

    public Date getTime() 
    {
        return time;
    }

    public void setPhoto(String photo) 
    {
        this.photo = photo;
    }

    public String getPhoto() 
    {
        return photo;
    }

    public void setIssuerId(Long issuerId) 
    {
        this.issuerId = issuerId;
    }

    public Long getIssuerId() 
    {
        return issuerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("titel", getTitel())
            .append("content", getContent())
            .append("time", getTime())
            .append("photo", getPhoto())
            .append("issuerId", getIssuerId())
            .toString();
    }
}
