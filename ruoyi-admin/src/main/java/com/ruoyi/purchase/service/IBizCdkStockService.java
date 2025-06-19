package com.ruoyi.purchase.service;

import java.util.List;
import com.ruoyi.purchase.domain.BizCdkStock;

/**
 * CDK库存Service接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface IBizCdkStockService 
{
    /**
     * 查询CDK库存
     * 
     * @param stockId CDK库存主键
     * @return CDK库存
     */
    public BizCdkStock selectBizCdkStockByStockId(Long stockId);

    /**
     * 查询CDK库存列表
     * 
     * @param bizCdkStock CDK库存
     * @return CDK库存集合
     */
    public List<BizCdkStock> selectBizCdkStockList(BizCdkStock bizCdkStock);

    /**
     * 新增CDK库存
     * 
     * @param bizCdkStock CDK库存
     * @return 结果
     */
    public int insertBizCdkStock(BizCdkStock bizCdkStock);

    /**
     * 修改CDK库存
     * 
     * @param bizCdkStock CDK库存
     * @return 结果
     */
    public int updateBizCdkStock(BizCdkStock bizCdkStock);

    /**
     * 批量删除CDK库存
     * 
     * @param stockIds 需要删除的CDK库存主键集合
     * @return 结果
     */
    public int deleteBizCdkStockByStockIds(Long[] stockIds);

    /**
     * 删除CDK库存信息
     * 
     * @param stockId CDK库存主键
     * @return 结果
     */
    public int deleteBizCdkStockByStockId(Long stockId);
}
