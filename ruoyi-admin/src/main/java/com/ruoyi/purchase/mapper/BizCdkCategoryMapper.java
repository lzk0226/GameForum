package com.ruoyi.purchase.mapper;

import java.util.List;
import com.ruoyi.purchase.domain.BizCdkCategory;

/**
 * CDK商品分类Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface BizCdkCategoryMapper 
{
    /**
     * 查询CDK商品分类
     * 
     * @param categoryId CDK商品分类主键
     * @return CDK商品分类
     */
    public BizCdkCategory selectBizCdkCategoryByCategoryId(Long categoryId);

    /**
     * 查询CDK商品分类列表
     * 
     * @param bizCdkCategory CDK商品分类
     * @return CDK商品分类集合
     */
    public List<BizCdkCategory> selectBizCdkCategoryList(BizCdkCategory bizCdkCategory);

    /**
     * 新增CDK商品分类
     * 
     * @param bizCdkCategory CDK商品分类
     * @return 结果
     */
    public int insertBizCdkCategory(BizCdkCategory bizCdkCategory);

    /**
     * 修改CDK商品分类
     * 
     * @param bizCdkCategory CDK商品分类
     * @return 结果
     */
    public int updateBizCdkCategory(BizCdkCategory bizCdkCategory);

    /**
     * 删除CDK商品分类
     * 
     * @param categoryId CDK商品分类主键
     * @return 结果
     */
    public int deleteBizCdkCategoryByCategoryId(Long categoryId);

    /**
     * 批量删除CDK商品分类
     * 
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCdkCategoryByCategoryIds(Long[] categoryIds);
}
