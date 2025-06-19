package com.ruoyi.shopping.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * CDK商品对象 biz_cdk_product
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public class CdkProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** CDK商品ID */
    private Long productId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 商品描述 */
    @Excel(name = "商品描述")
    private String productDescription;

    /** 关联游戏ID */
    @Excel(name = "关联游戏ID")
    private Long gameId;

    /** 商品类型（GAME_KEY：游戏本体，DLC：DLC内容，ITEM：游戏道具） */
    @Excel(name = "商品类型", readConverterExp = "G=AME_KEY：游戏本体，DLC：DLC内容，ITEM：游戏道具")
    private String productType;

    /** 原价 */
    @Excel(name = "原价")
    private BigDecimal originalPrice;

    /** 售价 */
    @Excel(name = "售价")
    private BigDecimal salePrice;

    /** 折扣率（1.00表示无折扣） */
    @Excel(name = "折扣率", readConverterExp = "1=.00表示无折扣")
    private BigDecimal discountRate;

    /** 库存数量 */
    @Excel(name = "库存数量")
    private Long stockCount;

    /** 已售数量 */
    @Excel(name = "已售数量")
    private Long soldCount;

    /** 平台（STEAM，EPIC，ORIGIN等） */
    @Excel(name = "平台", readConverterExp = "S=TEAM，EPIC，ORIGIN等")
    private String platform;

    /** 区域（CN，US，EU等） */
    @Excel(name = "区域", readConverterExp = "C=N，US，EU等")
    private String region;

    /** 是否自动发货（0否 1是） */
    @Excel(name = "是否自动发货", readConverterExp = "0=否,1=是")
    private String autoDelivery;

    /** 排序权重 */
    @Excel(name = "排序权重")
    private Long sortOrder;

    /** 状态（0正常 1停用 2售罄） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用,2=售罄")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public void setProductDescription(String productDescription) 
    {
        this.productDescription = productDescription;
    }

    public String getProductDescription() 
    {
        return productDescription;
    }

    public void setGameId(Long gameId) 
    {
        this.gameId = gameId;
    }

    public Long getGameId() 
    {
        return gameId;
    }

    public void setProductType(String productType) 
    {
        this.productType = productType;
    }

    public String getProductType() 
    {
        return productType;
    }

    public void setOriginalPrice(BigDecimal originalPrice) 
    {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getOriginalPrice() 
    {
        return originalPrice;
    }

    public void setSalePrice(BigDecimal salePrice) 
    {
        this.salePrice = salePrice;
    }

    public BigDecimal getSalePrice() 
    {
        return salePrice;
    }

    public void setDiscountRate(BigDecimal discountRate) 
    {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountRate() 
    {
        return discountRate;
    }

    public void setStockCount(Long stockCount) 
    {
        this.stockCount = stockCount;
    }

    public Long getStockCount() 
    {
        return stockCount;
    }

    public void setSoldCount(Long soldCount) 
    {
        this.soldCount = soldCount;
    }

    public Long getSoldCount() 
    {
        return soldCount;
    }

    public void setPlatform(String platform) 
    {
        this.platform = platform;
    }

    public String getPlatform() 
    {
        return platform;
    }

    public void setRegion(String region) 
    {
        this.region = region;
    }

    public String getRegion() 
    {
        return region;
    }

    public void setAutoDelivery(String autoDelivery) 
    {
        this.autoDelivery = autoDelivery;
    }

    public String getAutoDelivery() 
    {
        return autoDelivery;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
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
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("productDescription", getProductDescription())
            .append("gameId", getGameId())
            .append("productType", getProductType())
            .append("originalPrice", getOriginalPrice())
            .append("salePrice", getSalePrice())
            .append("discountRate", getDiscountRate())
            .append("stockCount", getStockCount())
            .append("soldCount", getSoldCount())
            .append("platform", getPlatform())
            .append("region", getRegion())
            .append("autoDelivery", getAutoDelivery())
            .append("sortOrder", getSortOrder())
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
