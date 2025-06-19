package com.ruoyi.forum.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 游戏类型对象 biz_game_type
 *
 * @author ruoyi
 * @date 2025-05-17
 */
public class BizGameType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 类型ID */
    private Long typeId;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String typeName;

    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public Long getTypeId()
    {
        return typeId;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("typeId", getTypeId())
            .append("typeName", getTypeName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
