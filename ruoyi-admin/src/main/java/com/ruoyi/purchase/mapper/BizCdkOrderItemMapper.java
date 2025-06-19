package com.ruoyi.purchase.mapper;

import java.util.List;
import com.ruoyi.purchase.domain.BizCdkOrderItem;

/**
 * CDK订单项Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface BizCdkOrderItemMapper 
{
    /**
     * 查询CDK订单项
     * 
     * @param itemId CDK订单项主键
     * @return CDK订单项
     */
    public BizCdkOrderItem selectBizCdkOrderItemByItemId(Long itemId);

    /**
     * 查询CDK订单项列表
     * 
     * @param bizCdkOrderItem CDK订单项
     * @return CDK订单项集合
     */
    public List<BizCdkOrderItem> selectBizCdkOrderItemList(BizCdkOrderItem bizCdkOrderItem);

    /**
     * 新增CDK订单项
     * 
     * @param bizCdkOrderItem CDK订单项
     * @return 结果
     */
    public int insertBizCdkOrderItem(BizCdkOrderItem bizCdkOrderItem);

    /**
     * 修改CDK订单项
     * 
     * @param bizCdkOrderItem CDK订单项
     * @return 结果
     */
    public int updateBizCdkOrderItem(BizCdkOrderItem bizCdkOrderItem);

    /**
     * 删除CDK订单项
     * 
     * @param itemId CDK订单项主键
     * @return 结果
     */
    public int deleteBizCdkOrderItemByItemId(Long itemId);

    /**
     * 批量删除CDK订单项
     * 
     * @param itemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCdkOrderItemByItemIds(Long[] itemIds);
}
