package com.ruoyi.purchase.service.impl;

import com.ruoyi.purchase.domain.BizProductCategory;
import com.ruoyi.purchase.mapper.BizProductCategoryMapper;
import com.ruoyi.purchase.service.IBizProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@Service
public class BizProductCategoryServiceImpl implements IBizProductCategoryService 
{
    @Autowired
    private BizProductCategoryMapper bizProductCategoryMapper;

    /**
     * 查询商品分类关联
     * 
     * @param productId 商品分类关联主键
     * @return 商品分类关联
     */
    @Override
    public BizProductCategory selectBizProductCategoryByProductId(Long productId)
    {
        return bizProductCategoryMapper.selectBizProductCategoryByProductId(productId);
    }

    /**
     * 查询商品分类关联列表
     * 
     * @param bizProductCategory 商品分类关联
     * @return 商品分类关联
     */
    @Override
    public List<BizProductCategory> selectBizProductCategoryList(BizProductCategory bizProductCategory)
    {
        return bizProductCategoryMapper.selectBizProductCategoryList(bizProductCategory);
    }

    /**
     * 新增商品分类关联
     * 
     * @param bizProductCategory 商品分类关联
     * @return 结果
     */
    @Override
    public int insertBizProductCategory(BizProductCategory bizProductCategory)
    {
        return bizProductCategoryMapper.insertBizProductCategory(bizProductCategory);
    }

    /**
     * 修改商品分类关联
     * 
     * @param bizProductCategory 商品分类关联
     * @return 结果
     */
    @Override
    public int updateBizProductCategory(BizProductCategory bizProductCategory)
    {
        return bizProductCategoryMapper.updateBizProductCategory(bizProductCategory);
    }

    /**
     * 批量删除商品分类关联
     * 
     * @param productIds 需要删除的商品分类关联主键
     * @return 结果
     */
    @Override
    public int deleteBizProductCategoryByProductIds(Long[] productIds)
    {
        return bizProductCategoryMapper.deleteBizProductCategoryByProductIds(productIds);
    }

    /**
     * 删除商品分类关联信息
     * 
     * @param productId 商品分类关联主键
     * @return 结果
     */
    @Override
    public int deleteBizProductCategoryByProductId(Long productId)
    {
        return bizProductCategoryMapper.deleteBizProductCategoryByProductId(productId);
    }
}
