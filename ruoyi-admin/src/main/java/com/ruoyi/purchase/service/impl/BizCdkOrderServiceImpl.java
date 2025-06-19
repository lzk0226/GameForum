package com.ruoyi.purchase.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.purchase.domain.BizCdkOrder;
import com.ruoyi.purchase.mapper.BizCdkOrderMapper;
import com.ruoyi.purchase.service.IBizCdkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CDK订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@Service
public class BizCdkOrderServiceImpl implements IBizCdkOrderService 
{
    @Autowired
    private BizCdkOrderMapper bizCdkOrderMapper;

    /**
     * 查询CDK订单
     * 
     * @param orderId CDK订单主键
     * @return CDK订单
     */
    @Override
    public BizCdkOrder selectBizCdkOrderByOrderId(Long orderId)
    {
        return bizCdkOrderMapper.selectBizCdkOrderByOrderId(orderId);
    }

    /**
     * 查询CDK订单列表
     * 
     * @param bizCdkOrder CDK订单
     * @return CDK订单
     */
    @Override
    public List<BizCdkOrder> selectBizCdkOrderList(BizCdkOrder bizCdkOrder)
    {
        return bizCdkOrderMapper.selectBizCdkOrderList(bizCdkOrder);
    }

    /**
     * 新增CDK订单
     * 
     * @param bizCdkOrder CDK订单
     * @return 结果
     */
    @Override
    public int insertBizCdkOrder(BizCdkOrder bizCdkOrder)
    {
        bizCdkOrder.setCreateTime(DateUtils.getNowDate());
        return bizCdkOrderMapper.insertBizCdkOrder(bizCdkOrder);
    }

    /**
     * 修改CDK订单
     * 
     * @param bizCdkOrder CDK订单
     * @return 结果
     */
    @Override
    public int updateBizCdkOrder(BizCdkOrder bizCdkOrder)
    {
        bizCdkOrder.setUpdateTime(DateUtils.getNowDate());
        return bizCdkOrderMapper.updateBizCdkOrder(bizCdkOrder);
    }

    /**
     * 批量删除CDK订单
     * 
     * @param orderIds 需要删除的CDK订单主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkOrderByOrderIds(Long[] orderIds)
    {
        return bizCdkOrderMapper.deleteBizCdkOrderByOrderIds(orderIds);
    }

    /**
     * 删除CDK订单信息
     * 
     * @param orderId CDK订单主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkOrderByOrderId(Long orderId)
    {
        return bizCdkOrderMapper.deleteBizCdkOrderByOrderId(orderId);
    }
}
