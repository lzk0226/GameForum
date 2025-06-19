package com.ruoyi.purchase.mapper;

import java.util.List;
import com.ruoyi.purchase.domain.BizCdkProduct;

/**
 * CDK商品Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface BizCdkProductMapper 
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
     * 删除CDK商品
     * 
     * @param productId CDK商品主键
     * @return 结果
     */
    public int deleteBizCdkProductByProductId(Long productId);

    /**
     * 批量删除CDK商品
     * 
     * @param productIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCdkProductByProductIds(Long[] productIds);
}
