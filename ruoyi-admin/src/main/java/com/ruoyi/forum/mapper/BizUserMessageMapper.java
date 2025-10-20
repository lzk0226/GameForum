package com.ruoyi.forum.mapper;

import java.util.List;
import com.ruoyi.forum.domain.BizUserMessage;

/**
 * 用户消息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface BizUserMessageMapper 
{
    /**
     * 查询用户消息
     * 
     * @param messageId 用户消息主键
     * @return 用户消息
     */
    public BizUserMessage selectBizUserMessageByMessageId(Long messageId);

    /**
     * 查询用户消息列表
     * 
     * @param bizUserMessage 用户消息
     * @return 用户消息集合
     */
    public List<BizUserMessage> selectBizUserMessageList(BizUserMessage bizUserMessage);

    /**
     * 新增用户消息
     * 
     * @param bizUserMessage 用户消息
     * @return 结果
     */
    public int insertBizUserMessage(BizUserMessage bizUserMessage);

    /**
     * 修改用户消息
     * 
     * @param bizUserMessage 用户消息
     * @return 结果
     */
    public int updateBizUserMessage(BizUserMessage bizUserMessage);

    /**
     * 删除用户消息
     * 
     * @param messageId 用户消息主键
     * @return 结果
     */
    public int deleteBizUserMessageByMessageId(Long messageId);

    /**
     * 批量删除用户消息
     * 
     * @param messageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizUserMessageByMessageIds(Long[] messageIds);
}
