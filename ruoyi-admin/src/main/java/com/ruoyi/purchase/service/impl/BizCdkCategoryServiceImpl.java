package com.ruoyi.purchase.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.purchase.domain.BizCdkCategory;
import com.ruoyi.purchase.mapper.BizCdkCategoryMapper;
import com.ruoyi.purchase.service.IBizCdkCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CDK商品分类Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@Service
public class BizCdkCategoryServiceImpl implements IBizCdkCategoryService 
{
    @Autowired
    private BizCdkCategoryMapper bizCdkCategoryMapper;

    /**
     * 查询CDK商品分类
     * 
     * @param categoryId CDK商品分类主键
     * @return CDK商品分类
     */
    @Override
    public BizCdkCategory selectBizCdkCategoryByCategoryId(Long categoryId)
    {
        return bizCdkCategoryMapper.selectBizCdkCategoryByCategoryId(categoryId);
    }

    /**
     * 查询CDK商品分类列表
     * 
     * @param bizCdkCategory CDK商品分类
     * @return CDK商品分类
     */
    @Override
    public List<BizCdkCategory> selectBizCdkCategoryList(BizCdkCategory bizCdkCategory)
    {
        return bizCdkCategoryMapper.selectBizCdkCategoryList(bizCdkCategory);
    }

    /**
     * 新增CDK商品分类
     * 
     * @param bizCdkCategory CDK商品分类
     * @return 结果
     */
    @Override
    public int insertBizCdkCategory(BizCdkCategory bizCdkCategory)
    {
        bizCdkCategory.setCreateTime(DateUtils.getNowDate());
        return bizCdkCategoryMapper.insertBizCdkCategory(bizCdkCategory);
    }

    /**
     * 修改CDK商品分类
     * 
     * @param bizCdkCategory CDK商品分类
     * @return 结果
     */
    @Override
    public int updateBizCdkCategory(BizCdkCategory bizCdkCategory)
    {
        bizCdkCategory.setUpdateTime(DateUtils.getNowDate());
        return bizCdkCategoryMapper.updateBizCdkCategory(bizCdkCategory);
    }

    /**
     * 批量删除CDK商品分类
     * 
     * @param categoryIds 需要删除的CDK商品分类主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkCategoryByCategoryIds(Long[] categoryIds)
    {
        return bizCdkCategoryMapper.deleteBizCdkCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除CDK商品分类信息
     * 
     * @param categoryId CDK商品分类主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkCategoryByCategoryId(Long categoryId)
    {
        return bizCdkCategoryMapper.deleteBizCdkCategoryByCategoryId(categoryId);
    }
}
