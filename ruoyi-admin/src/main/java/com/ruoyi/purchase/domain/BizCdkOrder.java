package com.ruoyi.purchase.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CDK订单对象 biz_cdk_order
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public class BizCdkOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 购买用户ID */
    @Excel(name = "购买用户ID")
    private Long userId;

    /** 商品ID */
    @Excel(name = "商品ID")
    private Long productId;

    /** 商品名称（冗余存储） */
    @Excel(name = "商品名称", readConverterExp = "冗=余存储")
    private String productName;

    /** 购买数量 */
    @Excel(name = "购买数量")
    private Long quantity;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal unitPrice;

    /** 总金额 */
    @Excel(name = "总金额")
    private BigDecimal totalAmount;

    /** 优惠金额 */
    @Excel(name = "优惠金额")
    private BigDecimal discountAmount;

    /** 实付金额 */
    @Excel(name = "实付金额")
    private BigDecimal actualAmount;

    /** 支付方式（ALIPAY，WECHAT，BALANCE等） */
    @Excel(name = "支付方式", readConverterExp = "A=LIPAY，WECHAT，BALANCE等")
    private String paymentMethod;

    /** 支付流水号 */
    @Excel(name = "支付流水号")
    private String paymentNo;

    /** 订单状态（0待支付 1已支付 2已发货 3已完成 4已取消 5已退款） */
    @Excel(name = "订单状态", readConverterExp = "0=待支付,1=已支付,2=已发货,3=已完成,4=已取消,5=已退款")
    private String orderStatus;

    /** 发货状态（0未发货 1已发货 2发货失败） */
    @Excel(name = "发货状态", readConverterExp = "0=未发货,1=已发货,2=发货失败")
    private String deliveryStatus;

    /** 联系邮箱 */
    @Excel(name = "联系邮箱")
    private String contactEmail;

    /** 买家留言 */
    @Excel(name = "买家留言")
    private String buyerNote;

    /** 管理员备注 */
    @Excel(name = "管理员备注")
    private String adminNote;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completeTime;

    /** 订单过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }

    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) 
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() 
    {
        return unitPrice;
    }

    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) 
    {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmount() 
    {
        return discountAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) 
    {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getActualAmount() 
    {
        return actualAmount;
    }

    public void setPaymentMethod(String paymentMethod) 
    {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() 
    {
        return paymentMethod;
    }

    public void setPaymentNo(String paymentNo) 
    {
        this.paymentNo = paymentNo;
    }

    public String getPaymentNo() 
    {
        return paymentNo;
    }

    public void setOrderStatus(String orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() 
    {
        return orderStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) 
    {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryStatus() 
    {
        return deliveryStatus;
    }

    public void setContactEmail(String contactEmail) 
    {
        this.contactEmail = contactEmail;
    }

    public String getContactEmail() 
    {
        return contactEmail;
    }

    public void setBuyerNote(String buyerNote) 
    {
        this.buyerNote = buyerNote;
    }

    public String getBuyerNote() 
    {
        return buyerNote;
    }

    public void setAdminNote(String adminNote) 
    {
        this.adminNote = adminNote;
    }

    public String getAdminNote() 
    {
        return adminNote;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public void setDeliveryTime(Date deliveryTime) 
    {
        this.deliveryTime = deliveryTime;
    }

    public Date getDeliveryTime() 
    {
        return deliveryTime;
    }

    public void setCompleteTime(Date completeTime) 
    {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() 
    {
        return completeTime;
    }

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("quantity", getQuantity())
            .append("unitPrice", getUnitPrice())
            .append("totalAmount", getTotalAmount())
            .append("discountAmount", getDiscountAmount())
            .append("actualAmount", getActualAmount())
            .append("paymentMethod", getPaymentMethod())
            .append("paymentNo", getPaymentNo())
            .append("orderStatus", getOrderStatus())
            .append("deliveryStatus", getDeliveryStatus())
            .append("contactEmail", getContactEmail())
            .append("buyerNote", getBuyerNote())
            .append("adminNote", getAdminNote())
            .append("payTime", getPayTime())
            .append("deliveryTime", getDeliveryTime())
            .append("completeTime", getCompleteTime())
            .append("expireTime", getExpireTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
