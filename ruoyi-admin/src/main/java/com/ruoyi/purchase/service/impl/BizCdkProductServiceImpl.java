package com.ruoyi.purchase.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.purchase.domain.BizCdkProduct;
import com.ruoyi.purchase.mapper.BizCdkProductMapper;
import com.ruoyi.purchase.service.IBizCdkProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CDK商品Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@Service
public class BizCdkProductServiceImpl implements IBizCdkProductService 
{
    @Autowired
    private BizCdkProductMapper bizCdkProductMapper;

    /**
     * 查询CDK商品
     * 
     * @param productId CDK商品主键
     * @return CDK商品
     */
    @Override
    public BizCdkProduct selectBizCdkProductByProductId(Long productId)
    {
        return bizCdkProductMapper.selectBizCdkProductByProductId(productId);
    }

    /**
     * 查询CDK商品列表
     * 
     * @param bizCdkProduct CDK商品
     * @return CDK商品
     */
    @Override
    public List<BizCdkProduct> selectBizCdkProductList(BizCdkProduct bizCdkProduct)
    {
        return bizCdkProductMapper.selectBizCdkProductList(bizCdkProduct);
    }

    /**
     * 新增CDK商品
     * 
     * @param bizCdkProduct CDK商品
     * @return 结果
     */
    @Override
    public int insertBizCdkProduct(BizCdkProduct bizCdkProduct)
    {
        bizCdkProduct.setCreateTime(DateUtils.getNowDate());
        return bizCdkProductMapper.insertBizCdkProduct(bizCdkProduct);
    }

    /**
     * 修改CDK商品
     * 
     * @param bizCdkProduct CDK商品
     * @return 结果
     */
    @Override
    public int updateBizCdkProduct(BizCdkProduct bizCdkProduct)
    {
        bizCdkProduct.setUpdateTime(DateUtils.getNowDate());
        return bizCdkProductMapper.updateBizCdkProduct(bizCdkProduct);
    }

    /**
     * 批量删除CDK商品
     * 
     * @param productIds 需要删除的CDK商品主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkProductByProductIds(Long[] productIds)
    {
        return bizCdkProductMapper.deleteBizCdkProductByProductIds(productIds);
    }

    /**
     * 删除CDK商品信息
     * 
     * @param productId CDK商品主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkProductByProductId(Long productId)
    {
        return bizCdkProductMapper.deleteBizCdkProductByProductId(productId);
    }
}
