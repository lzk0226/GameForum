package com.ruoyi.purchase.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.purchase.domain.BizUserBalance;
import com.ruoyi.purchase.mapper.BizUserBalanceMapper;
import com.ruoyi.purchase.service.IBizUserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户余额Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@Service
public class BizUserBalanceServiceImpl implements IBizUserBalanceService 
{
    @Autowired
    private BizUserBalanceMapper bizUserBalanceMapper;

    /**
     * 查询用户余额
     * 
     * @param userId 用户余额主键
     * @return 用户余额
     */
    @Override
    public BizUserBalance selectBizUserBalanceByUserId(Long userId)
    {
        return bizUserBalanceMapper.selectBizUserBalanceByUserId(userId);
    }

    /**
     * 查询用户余额列表
     * 
     * @param bizUserBalance 用户余额
     * @return 用户余额
     */
    @Override
    public List<BizUserBalance> selectBizUserBalanceList(BizUserBalance bizUserBalance)
    {
        return bizUserBalanceMapper.selectBizUserBalanceList(bizUserBalance);
    }

    /**
     * 新增用户余额
     * 
     * @param bizUserBalance 用户余额
     * @return 结果
     */
    @Override
    public int insertBizUserBalance(BizUserBalance bizUserBalance)
    {
        bizUserBalance.setCreateTime(DateUtils.getNowDate());
        return bizUserBalanceMapper.insertBizUserBalance(bizUserBalance);
    }

    /**
     * 修改用户余额
     * 
     * @param bizUserBalance 用户余额
     * @return 结果
     */
    @Override
    public int updateBizUserBalance(BizUserBalance bizUserBalance)
    {
        bizUserBalance.setUpdateTime(DateUtils.getNowDate());
        return bizUserBalanceMapper.updateBizUserBalance(bizUserBalance);
    }

    /**
     * 批量删除用户余额
     * 
     * @param userIds 需要删除的用户余额主键
     * @return 结果
     */
    @Override
    public int deleteBizUserBalanceByUserIds(Long[] userIds)
    {
        return bizUserBalanceMapper.deleteBizUserBalanceByUserIds(userIds);
    }

    /**
     * 删除用户余额信息
     * 
     * @param userId 用户余额主键
     * @return 结果
     */
    @Override
    public int deleteBizUserBalanceByUserId(Long userId)
    {
        return bizUserBalanceMapper.deleteBizUserBalanceByUserId(userId);
    }
}
