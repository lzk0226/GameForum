package com.ruoyi.purchase.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.purchase.domain.BizCdkOrderItem;
import com.ruoyi.purchase.mapper.BizCdkOrderItemMapper;
import com.ruoyi.purchase.service.IBizCdkOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CDK订单项Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@Service
public class BizCdkOrderItemServiceImpl implements IBizCdkOrderItemService 
{
    @Autowired
    private BizCdkOrderItemMapper bizCdkOrderItemMapper;

    /**
     * 查询CDK订单项
     * 
     * @param itemId CDK订单项主键
     * @return CDK订单项
     */
    @Override
    public BizCdkOrderItem selectBizCdkOrderItemByItemId(Long itemId)
    {
        return bizCdkOrderItemMapper.selectBizCdkOrderItemByItemId(itemId);
    }

    /**
     * 查询CDK订单项列表
     * 
     * @param bizCdkOrderItem CDK订单项
     * @return CDK订单项
     */
    @Override
    public List<BizCdkOrderItem> selectBizCdkOrderItemList(BizCdkOrderItem bizCdkOrderItem)
    {
        return bizCdkOrderItemMapper.selectBizCdkOrderItemList(bizCdkOrderItem);
    }

    /**
     * 新增CDK订单项
     * 
     * @param bizCdkOrderItem CDK订单项
     * @return 结果
     */
    @Override
    public int insertBizCdkOrderItem(BizCdkOrderItem bizCdkOrderItem)
    {
        bizCdkOrderItem.setCreateTime(DateUtils.getNowDate());
        return bizCdkOrderItemMapper.insertBizCdkOrderItem(bizCdkOrderItem);
    }

    /**
     * 修改CDK订单项
     * 
     * @param bizCdkOrderItem CDK订单项
     * @return 结果
     */
    @Override
    public int updateBizCdkOrderItem(BizCdkOrderItem bizCdkOrderItem)
    {
        return bizCdkOrderItemMapper.updateBizCdkOrderItem(bizCdkOrderItem);
    }

    /**
     * 批量删除CDK订单项
     * 
     * @param itemIds 需要删除的CDK订单项主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkOrderItemByItemIds(Long[] itemIds)
    {
        return bizCdkOrderItemMapper.deleteBizCdkOrderItemByItemIds(itemIds);
    }

    /**
     * 删除CDK订单项信息
     * 
     * @param itemId CDK订单项主键
     * @return 结果
     */
    @Override
    public int deleteBizCdkOrderItemByItemId(Long itemId)
    {
        return bizCdkOrderItemMapper.deleteBizCdkOrderItemByItemId(itemId);
    }
}
