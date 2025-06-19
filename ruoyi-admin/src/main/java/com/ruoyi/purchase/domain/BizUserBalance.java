package com.ruoyi.purchase.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户余额对象 biz_user_balance
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public class BizUserBalance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 余额 */
    @Excel(name = "余额")
    private BigDecimal balance;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private BigDecimal frozenAmount;

    /** 累计充值 */
    @Excel(name = "累计充值")
    private BigDecimal totalRecharge;

    /** 累计消费 */
    @Excel(name = "累计消费")
    private BigDecimal totalConsume;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setBalance(BigDecimal balance) 
    {
        this.balance = balance;
    }

    public BigDecimal getBalance() 
    {
        return balance;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) 
    {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getFrozenAmount() 
    {
        return frozenAmount;
    }

    public void setTotalRecharge(BigDecimal totalRecharge) 
    {
        this.totalRecharge = totalRecharge;
    }

    public BigDecimal getTotalRecharge() 
    {
        return totalRecharge;
    }

    public void setTotalConsume(BigDecimal totalConsume) 
    {
        this.totalConsume = totalConsume;
    }

    public BigDecimal getTotalConsume() 
    {
        return totalConsume;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("balance", getBalance())
            .append("frozenAmount", getFrozenAmount())
            .append("totalRecharge", getTotalRecharge())
            .append("totalConsume", getTotalConsume())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
