package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 游戏对象 biz_game
 *
 * @author ruoyi
 * @date 2025-05-17
 */
public class BizGame extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 游戏ID */
    private Long gameId;

    /** 游戏名称 */
    @Excel(name = "游戏名称")
    private String gameName;

    /** 游戏简介 */
    @Excel(name = "游戏简介")
    private String gameDescription;

    /** 游戏类型ID */
    @Excel(name = "游戏类型ID")
    private Long gameTypeId;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setGameId(Long gameId)
    {
        this.gameId = gameId;
    }

    public Long getGameId()
    {
        return gameId;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameDescription(String gameDescription)
    {
        this.gameDescription = gameDescription;
    }

    public String getGameDescription()
    {
        return gameDescription;
    }

    public void setGameTypeId(Long gameTypeId)
    {
        this.gameTypeId = gameTypeId;
    }

    public Long getGameTypeId()
    {
        return gameTypeId;
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
            .append("gameId", getGameId())
            .append("gameName", getGameName())
            .append("gameDescription", getGameDescription())
            .append("gameTypeId", getGameTypeId())
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
