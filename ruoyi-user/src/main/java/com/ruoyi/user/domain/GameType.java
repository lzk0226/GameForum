package com.ruoyi.user.domain;

import java.util.Date;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/24下午 6:23
 * @Author : SockLightDust
 */
public class GameType {

    /** 类型ID */
    private Integer typeId;

    /** 类型名称 */
    private String typeName;

    /** 创建时间 */
    private Date createTime;

    /** 备注 */
    private String remark;

    // 构造函数
    public GameType() {}

    // Getter 和 Setter 方法
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}