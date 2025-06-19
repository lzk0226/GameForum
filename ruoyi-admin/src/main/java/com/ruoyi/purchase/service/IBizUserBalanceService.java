package com.ruoyi.purchase.service;

import java.util.List;
import com.ruoyi.purchase.domain.BizUserBalance;

/**
 * 用户余额Service接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface IBizUserBalanceService 
{
    /**
     * 查询用户余额
     * 
     * @param userId 用户余额主键
     * @return 用户余额
     */
    public BizUserBalance selectBizUserBalanceByUserId(Long userId);

    /**
     * 查询用户余额列表
     * 
     * @param bizUserBalance 用户余额
     * @return 用户余额集合
     */
    public List<BizUserBalance> selectBizUserBalanceList(BizUserBalance bizUserBalance);

    /**
     * 新增用户余额
     * 
     * @param bizUserBalance 用户余额
     * @return 结果
     */
    public int insertBizUserBalance(BizUserBalance bizUserBalance);

    /**
     * 修改用户余额
     * 
     * @param bizUserBalance 用户余额
     * @return 结果
     */
    public int updateBizUserBalance(BizUserBalance bizUserBalance);

    /**
     * 批量删除用户余额
     * 
     * @param userIds 需要删除的用户余额主键集合
     * @return 结果
     */
    public int deleteBizUserBalanceByUserIds(Long[] userIds);

    /**
     * 删除用户余额信息
     * 
     * @param userId 用户余额主键
     * @return 结果
     */
    public int deleteBizUserBalanceByUserId(Long userId);
}
