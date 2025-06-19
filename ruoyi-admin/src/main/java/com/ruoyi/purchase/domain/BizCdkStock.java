package com.ruoyi.purchase.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CDK库存对象 biz_cdk_stock
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public class BizCdkStock extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库存ID */
    private Long stockId;

    /** 商品ID */
    @Excel(name = "商品ID")
    private Long productId;

    /** CDK码 */
    @Excel(name = "CDK码")
    private String cdkCode;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchNumber;

    /** 成本价 */
    @Excel(name = "成本价")
    private BigDecimal costPrice;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 状态（0未售出 1已售出 2已冻结 3已过期） */
    @Excel(name = "状态", readConverterExp = "0=未售出,1=已售出,2=已冻结,3=已过期")
    private String status;

    /** 售出时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "售出时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date soldTime;

    /** 售出订单ID */
    @Excel(name = "售出订单ID")
    private Long soldOrderId;

    public void setStockId(Long stockId) 
    {
        this.stockId = stockId;
    }

    public Long getStockId() 
    {
        return stockId;
    }

    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }

    public void setCdkCode(String cdkCode) 
    {
        this.cdkCode = cdkCode;
    }

    public String getCdkCode() 
    {
        return cdkCode;
    }

    public void setBatchNumber(String batchNumber) 
    {
        this.batchNumber = batchNumber;
    }

    public String getBatchNumber() 
    {
        return batchNumber;
    }

    public void setCostPrice(BigDecimal costPrice) 
    {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostPrice() 
    {
        return costPrice;
    }

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSoldTime(Date soldTime) 
    {
        this.soldTime = soldTime;
    }

    public Date getSoldTime() 
    {
        return soldTime;
    }

    public void setSoldOrderId(Long soldOrderId) 
    {
        this.soldOrderId = soldOrderId;
    }

    public Long getSoldOrderId() 
    {
        return soldOrderId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("stockId", getStockId())
            .append("productId", getProductId())
            .append("cdkCode", getCdkCode())
            .append("batchNumber", getBatchNumber())
            .append("costPrice", getCostPrice())
            .append("expireTime", getExpireTime())
            .append("status", getStatus())
            .append("soldTime", getSoldTime())
            .append("soldOrderId", getSoldOrderId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
