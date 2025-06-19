package com.ruoyi.purchase.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 余额变动日志对象 biz_balance_log
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public class BizBalanceLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 变动类型（RECHARGE：充值，CONSUME：消费，REFUND：退款，FREEZE：冻结，UNFREEZE：解冻） */
    @Excel(name = "变动类型", readConverterExp = "R=ECHARGE：充值，CONSUME：消费，REFUND：退款，FREEZE：冻结，UNFREEZE：解冻")
    private String changeType;

    /** 变动金额 */
    @Excel(name = "变动金额")
    private BigDecimal changeAmount;

    /** 变动前余额 */
    @Excel(name = "变动前余额")
    private BigDecimal beforeBalance;

    /** 变动后余额 */
    @Excel(name = "变动后余额")
    private BigDecimal afterBalance;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long relatedOrderId;

    /** 变动说明 */
    @Excel(name = "变动说明")
    private String description;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setChangeType(String changeType) 
    {
        this.changeType = changeType;
    }

    public String getChangeType() 
    {
        return changeType;
    }

    public void setChangeAmount(BigDecimal changeAmount) 
    {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getChangeAmount() 
    {
        return changeAmount;
    }

    public void setBeforeBalance(BigDecimal beforeBalance) 
    {
        this.beforeBalance = beforeBalance;
    }

    public BigDecimal getBeforeBalance() 
    {
        return beforeBalance;
    }

    public void setAfterBalance(BigDecimal afterBalance) 
    {
        this.afterBalance = afterBalance;
    }

    public BigDecimal getAfterBalance() 
    {
        return afterBalance;
    }

    public void setRelatedOrderId(Long relatedOrderId) 
    {
        this.relatedOrderId = relatedOrderId;
    }

    public Long getRelatedOrderId() 
    {
        return relatedOrderId;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("userId", getUserId())
            .append("changeType", getChangeType())
            .append("changeAmount", getChangeAmount())
            .append("beforeBalance", getBeforeBalance())
            .append("afterBalance", getAfterBalance())
            .append("relatedOrderId", getRelatedOrderId())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .toString();
    }
}
