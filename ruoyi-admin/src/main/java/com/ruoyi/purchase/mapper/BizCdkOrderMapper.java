package com.ruoyi.purchase.mapper;

import java.util.List;
import com.ruoyi.purchase.domain.BizCdkOrder;

/**
 * CDK订单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface BizCdkOrderMapper 
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
     * 删除CDK订单
     * 
     * @param orderId CDK订单主键
     * @return 结果
     */
    public int deleteBizCdkOrderByOrderId(Long orderId);

    /**
     * 批量删除CDK订单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCdkOrderByOrderIds(Long[] orderIds);
}
