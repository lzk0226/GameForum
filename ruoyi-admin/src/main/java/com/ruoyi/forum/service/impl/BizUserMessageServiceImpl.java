package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizUserMessageMapper;
import com.ruoyi.forum.domain.BizUserMessage;
import com.ruoyi.forum.service.IBizUserMessageService;

/**
 * 用户消息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class BizUserMessageServiceImpl implements IBizUserMessageService 
{
    @Autowired
    private BizUserMessageMapper bizUserMessageMapper;

    /**
     * 查询用户消息
     * 
     * @param messageId 用户消息主键
     * @return 用户消息
     */
    @Override
    public BizUserMessage selectBizUserMessageByMessageId(Long messageId)
    {
        return bizUserMessageMapper.selectBizUserMessageByMessageId(messageId);
    }

    /**
     * 查询用户消息列表
     * 
     * @param bizUserMessage 用户消息
     * @return 用户消息
     */
    @Override
    public List<BizUserMessage> selectBizUserMessageList(BizUserMessage bizUserMessage)
    {
        return bizUserMessageMapper.selectBizUserMessageList(bizUserMessage);
    }

    /**
     * 新增用户消息
     * 
     * @param bizUserMessage 用户消息
     * @return 结果
     */
    @Override
    public int insertBizUserMessage(BizUserMessage bizUserMessage)
    {
        bizUserMessage.setCreateTime(DateUtils.getNowDate());
        return bizUserMessageMapper.insertBizUserMessage(bizUserMessage);
    }

    /**
     * 修改用户消息
     * 
     * @param bizUserMessage 用户消息
     * @return 结果
     */
    @Override
    public int updateBizUserMessage(BizUserMessage bizUserMessage)
    {
        return bizUserMessageMapper.updateBizUserMessage(bizUserMessage);
    }

    /**
     * 批量删除用户消息
     * 
     * @param messageIds 需要删除的用户消息主键
     * @return 结果
     */
    @Override
    public int deleteBizUserMessageByMessageIds(Long[] messageIds)
    {
        return bizUserMessageMapper.deleteBizUserMessageByMessageIds(messageIds);
    }

    /**
     * 删除用户消息信息
     * 
     * @param messageId 用户消息主键
     * @return 结果
     */
    @Override
    public int deleteBizUserMessageByMessageId(Long messageId)
    {
        return bizUserMessageMapper.deleteBizUserMessageByMessageId(messageId);
    }
}
