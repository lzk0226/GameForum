package com.ruoyi.purchase.service;

import java.util.List;
import com.ruoyi.purchase.domain.BizBalanceLog;

/**
 * 余额变动日志Service接口
 * 
 * @author ruoyi
 * @date 2025-05-23
 */
public interface IBizBalanceLogService 
{
    /**
     * 查询余额变动日志
     * 
     * @param logId 余额变动日志主键
     * @return 余额变动日志
     */
    public BizBalanceLog selectBizBalanceLogByLogId(Long logId);

    /**
     * 查询余额变动日志列表
     * 
     * @param bizBalanceLog 余额变动日志
     * @return 余额变动日志集合
     */
    public List<BizBalanceLog> selectBizBalanceLogList(BizBalanceLog bizBalanceLog);

    /**
     * 新增余额变动日志
     * 
     * @param bizBalanceLog 余额变动日志
     * @return 结果
     */
    public int insertBizBalanceLog(BizBalanceLog bizBalanceLog);

    /**
     * 修改余额变动日志
     * 
     * @param bizBalanceLog 余额变动日志
     * @return 结果
     */
    public int updateBizBalanceLog(BizBalanceLog bizBalanceLog);

    /**
     * 批量删除余额变动日志
     * 
     * @param logIds 需要删除的余额变动日志主键集合
     * @return 结果
     */
    public int deleteBizBalanceLogByLogIds(Long[] logIds);

    /**
     * 删除余额变动日志信息
     * 
     * @param logId 余额变动日志主键
     * @return 结果
     */
    public int deleteBizBalanceLogByLogId(Long logId);
}
