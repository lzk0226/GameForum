package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 论坛版块对象 biz_section
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public class BizSection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 版块ID */
    private Long sectionId;

    /** 版块名称 */
    @Excel(name = "版块名称")
    private String sectionName;

    /** 版块描述 */
    @Excel(name = "版块描述")
    private String sectionDescription;

    /** 关联游戏ID */
    @Excel(name = "关联游戏ID")
    private Long gameId;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setSectionId(Long sectionId) 
    {
        this.sectionId = sectionId;
    }

    public Long getSectionId() 
    {
        return sectionId;
    }

    public void setSectionName(String sectionName) 
    {
        this.sectionName = sectionName;
    }

    public String getSectionName() 
    {
        return sectionName;
    }

    public void setSectionDescription(String sectionDescription) 
    {
        this.sectionDescription = sectionDescription;
    }

    public String getSectionDescription() 
    {
        return sectionDescription;
    }

    public void setGameId(Long gameId) 
    {
        this.gameId = gameId;
    }

    public Long getGameId() 
    {
        return gameId;
    }

    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
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
            .append("sectionId", getSectionId())
            .append("sectionName", getSectionName())
            .append("sectionDescription", getSectionDescription())
            .append("gameId", getGameId())
            .append("orderNum", getOrderNum())
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
