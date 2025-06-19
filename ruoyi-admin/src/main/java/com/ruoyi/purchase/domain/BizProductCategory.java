package com.ruoyi.purchase.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品分类关联对象 biz_product_category
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public class BizProductCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    private Long productId;

    /** 分类ID */
    private Long categoryId;

    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("categoryId", getCategoryId())
            .toString();
    }
}
