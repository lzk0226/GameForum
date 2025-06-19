package com.ruoyi.purchase.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.purchase.domain.BizBalanceLog;
import com.ruoyi.purchase.mapper.BizBalanceLogMapper;
import com.ruoyi.purchase.service.IBizBalanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 余额变动日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
@Service
public class BizBalanceLogServiceImpl implements IBizBalanceLogService 
{
    @Autowired
    private BizBalanceLogMapper bizBalanceLogMapper;

    /**
     * 查询余额变动日志
     *
     * @param logId 余额变动日志主键
     * @return 余额变动日志
     */
    @Override
    public BizBalanceLog selectBizBalanceLogByLogId(Long logId)
    {
        return bizBalanceLogMapper.selectBizBalanceLogByLogId(logId);
    }

    /**
     * 查询余额变动日志列表
     * 
     * @param bizBalanceLog 余额变动日志
     * @return 余额变动日志
     */
    @Override
    public List<BizBalanceLog> selectBizBalanceLogList(BizBalanceLog bizBalanceLog)
    {
        return bizBalanceLogMapper.selectBizBalanceLogList(bizBalanceLog);
    }

    /**
     * 新增余额变动日志
     * 
     * @param bizBalanceLog 余额变动日志
     * @return 结果
     */
    @Override
    public int insertBizBalanceLog(BizBalanceLog bizBalanceLog)
    {
        bizBalanceLog.setCreateTime(DateUtils.getNowDate());
        return bizBalanceLogMapper.insertBizBalanceLog(bizBalanceLog);
    }

    /**
     * 修改余额变动日志
     * 
     * @param bizBalanceLog 余额变动日志
     * @return 结果
     */
    @Override
    public int updateBizBalanceLog(BizBalanceLog bizBalanceLog)
    {
        return bizBalanceLogMapper.updateBizBalanceLog(bizBalanceLog);
    }

    /**
     * 批量删除余额变动日志
     * 
     * @param logIds 需要删除的余额变动日志主键
     * @return 结果
     */
    @Override
    public int deleteBizBalanceLogByLogIds(Long[] logIds)
    {
        return bizBalanceLogMapper.deleteBizBalanceLogByLogIds(logIds);
    }

    /**
     * 删除余额变动日志信息
     * 
     * @param logId 余额变动日志主键
     * @return 结果
     */
    @Override
    public int deleteBizBalanceLogByLogId(Long logId)
    {
        return bizBalanceLogMapper.deleteBizBalanceLogByLogId(logId);
    }
}
