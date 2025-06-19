package com.ruoyi.purchase.mapper;

import java.util.List;
import com.ruoyi.purchase.domain.BizProductCategory;

/**
 * 商品分类关联Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface BizProductCategoryMapper 
{
    /**
     * 查询商品分类关联
     * 
     * @param productId 商品分类关联主键
     * @return 商品分类关联
     */
    public BizProductCategory selectBizProductCategoryByProductId(Long productId);

    /**
     * 查询商品分类关联列表
     * 
     * @param bizProductCategory 商品分类关联
     * @return 商品分类关联集合
     */
    public List<BizProductCategory> selectBizProductCategoryList(BizProductCategory bizProductCategory);

    /**
     * 新增商品分类关联
     * 
     * @param bizProductCategory 商品分类关联
     * @return 结果
     */
    public int insertBizProductCategory(BizProductCategory bizProductCategory);

    /**
     * 修改商品分类关联
     * 
     * @param bizProductCategory 商品分类关联
     * @return 结果
     */
    public int updateBizProductCategory(BizProductCategory bizProductCategory);

    /**
     * 删除商品分类关联
     * 
     * @param productId 商品分类关联主键
     * @return 结果
     */
    public int deleteBizProductCategoryByProductId(Long productId);

    /**
     * 批量删除商品分类关联
     * 
     * @param productIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizProductCategoryByProductIds(Long[] productIds);
}
