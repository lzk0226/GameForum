package com.ruoyi.purchase.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CDK订单项对象 biz_cdk_order_item
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public class BizCdkOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单项ID */
    private Long itemId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 库存ID */
    @Excel(name = "库存ID")
    private Long stockId;

    /** CDK码 */
    @Excel(name = "CDK码")
    private String cdkCode;

    /** 发货状态（0未发货 1已发货） */
    @Excel(name = "发货状态", readConverterExp = "0=未发货,1=已发货")
    private String deliveryStatus;

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryTime;

    public void setItemId(Long itemId) 
    {
        this.itemId = itemId;
    }

    public Long getItemId() 
    {
        return itemId;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setStockId(Long stockId) 
    {
        this.stockId = stockId;
    }

    public Long getStockId() 
    {
        return stockId;
    }

    public void setCdkCode(String cdkCode) 
    {
        this.cdkCode = cdkCode;
    }

    public String getCdkCode() 
    {
        return cdkCode;
    }

    public void setDeliveryStatus(String deliveryStatus) 
    {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryStatus() 
    {
        return deliveryStatus;
    }

    public void setDeliveryTime(Date deliveryTime) 
    {
        this.deliveryTime = deliveryTime;
    }

    public Date getDeliveryTime() 
    {
        return deliveryTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("orderId", getOrderId())
            .append("stockId", getStockId())
            .append("cdkCode", getCdkCode())
            .append("deliveryStatus", getDeliveryStatus())
            .append("deliveryTime", getDeliveryTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
