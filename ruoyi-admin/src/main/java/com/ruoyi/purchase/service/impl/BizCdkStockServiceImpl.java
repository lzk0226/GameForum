package com.ruoyi.purchase.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.purchase.domain.BizCdkStock;
import com.ruoyi.purchase.mapper.BizCdkStockMapper;
import com.ruoyi.purchase.service.IBizCdkStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CDK库存Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@Service
public class BizCdkStockServiceImpl implements IBizCdkStockService 
{
    @Autowired
    private BizCdkStockMapper bizCdkStockMapper;

    /**
     * 查询CDK库存
     * 
     * @param stockId CDK库存主键
     * @return CDK库存
     */
    @Override
    public BizCdkStock selectBizCdkStockByStockId(Long stockId)
    {
        return bizCdkStockMapper.selectBizCdkStockByStockId(stockId);
    }

    /**
     * 查询CDK库存列表
     * 
     * @param bizCdkStock CDK库存
     * @return CDK库存
     */
    @Override
    public List<BizCdkStock> selectBizCdkStockList(BizCdkStock bizCdkStock)
    {
        return bizCdkStockMapper.selectBizCdkStockList(bizCdkStock);
    }

    /**
     * 新增CDK库存
     * 
     * @param bizCdkStock CDK库存
     * @return 结果
     */
    @Override
    public int insertBizCdkStock(BizCdkStock bizCdkStock)
    {
        bizCdkStock.setCreateTime(DateUtils.getNowDate());
        return bizCdkStockMapper.insertBizCdkStock(bizCdkStock);
    }

    /**
     * 修改CDK库存
     * 
     * @param bizCdkStock CDK库存
     * @return 结果
     */
    @Override
    public int updateBizCdkStock(BizCdkStock bizCdkStock)
    {
        bizCdkStock.setUpdateTime(DateUtils.getNowDate());
        return bizCdkStockMapper.updateBizCdkStock(bizCdkStock);
    }

    /**
     * 批量删除CDK库存
     * 
     * @param stockIds 需要删除的CDK库存主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkStockByStockIds(Long[] stockIds)
    {
        return bizCdkStockMapper.deleteBizCdkStockByStockIds(stockIds);
    }

    /**
     * 删除CDK库存信息
     * 
     * @param stockId CDK库存主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkStockByStockId(Long stockId)
    {
        return bizCdkStockMapper.deleteBizCdkStockByStockId(stockId);
    }
}
