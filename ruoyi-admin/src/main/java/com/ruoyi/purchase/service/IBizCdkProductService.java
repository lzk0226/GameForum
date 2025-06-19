package com.ruoyi.purchase.service;

import java.util.List;
import com.ruoyi.purchase.domain.BizCdkProduct;

/**
 * CDK商品Service接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface IBizCdkProductService 
{
    /**
     * 查询CDK商品
     * 
     * @param productId CDK商品主键
     * @return CDK商品
     */
    public BizCdkProduct selectBizCdkProductByProductId(Long productId);

    /**
     * 查询CDK商品列表
     * 
     * @param bizCdkProduct CDK商品
     * @return CDK商品集合
     */
    public List<BizCdkProduct> selectBizCdkProductList(BizCdkProduct bizCdkProduct);

    /**
     * 新增CDK商品
     * 
     * @param bizCdkProduct CDK商品
     * @return 结果
     */
    public int insertBizCdkProduct(BizCdkProduct bizCdkProduct);

    /**
     * 修改CDK商品
     * 
     * @param bizCdkProduct CDK商品
     * @return 结果
     */
    public int updateBizCdkProduct(BizCdkProduct bizCdkProduct);

    /**
     * 批量删除CDK商品
     * 
     * @param productIds 需要删除的CDK商品主键集合
     * @return 结果
     */
    public int deleteBizCdkProductByProductIds(Long[] productIds);

    /**
     * 删除CDK商品信息
     * 
     * @param productId CDK商品主键
     * @return 结果
     */
    public int deleteBizCdkProductByProductId(Long productId);
}
