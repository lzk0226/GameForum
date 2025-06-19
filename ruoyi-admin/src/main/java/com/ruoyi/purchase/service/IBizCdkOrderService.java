package com.ruoyi.purchase.service;

import java.util.List;
import com.ruoyi.purchase.domain.BizCdkOrder;

/**
 * CDK订单Service接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface IBizCdkOrderService 
{
    /**
     * 查询CDK订单
     * 
     * @param orderId CDK订单主键
     * @return CDK订单
     */
    public BizCdkOrder selectBizCdkOrderByOrderId(Long orderId);

    /**
     * 查询CDK订单列表
     * 
     * @param bizCdkOrder CDK订单
     * @return CDK订单集合
     */
    public List<BizCdkOrder> selectBizCdkOrderList(BizCdkOrder bizCdkOrder);

    /**
     * 新增CDK订单
     * 
     * @param bizCdkOrder CDK订单
     * @return 结果
     */
    public int insertBizCdkOrder(BizCdkOrder bizCdkOrder);

    /**
     * 修改CDK订单
     * 
     * @param bizCdkOrder CDK订单
     * @return 结果
     */
    public int updateBizCdkOrder(BizCdkOrder bizCdkOrder);

    /**
     * 批量删除CDK订单
     * 
     * @param orderIds 需要删除的CDK订单主键集合
     * @return 结果
     */
    public int deleteBizCdkOrderByOrderIds(Long[] orderIds);

    /**
     * 删除CDK订单信息
     * 
     * @param orderId CDK订单主键
     * @return 结果
     */
    public int deleteBizCdkOrderByOrderId(Long orderId);
}
