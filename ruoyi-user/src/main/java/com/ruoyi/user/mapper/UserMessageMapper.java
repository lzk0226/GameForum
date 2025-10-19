package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.UserMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 用户消息Mapper接口
 * 对应XML：UserMessageMapper.xml
 */
@Mapper
public interface UserMessageMapper {

    /**
     * 查询用户消息列表
     * @param userMessage 查询条件
     * @return 消息列表
     */
    List<UserMessage> selectMessageList(UserMessage userMessage);

    /**
     * 查询用户未读消息列表
     * @param userId 用户ID
     * @return 未读消息列表
     */
    List<UserMessage> selectUnreadMessageList(@Param("userId") Long userId);

    /**
     * 查询消息详情
     * @param messageId 消息ID
     * @return 消息详情
     */
    UserMessage selectMessageById(@Param("messageId") Long messageId);

    /**
     * 查询用户未读消息数
     * @param userId 用户ID
     * @return 未读消息数量
     */
    int countUnreadByUserId(@Param("userId") Long userId);

    /**
     * 按类型统计未读消息数
     * @param userId 用户ID
     * @return 每种类型的未读消息统计
     */
    List<HashMap<String, Object>> countUnreadByType(@Param("userId") Long userId);

    /**
     * 新增消息
     * @param userMessage 消息实体
     * @return 插入结果
     */
    int insertMessage(UserMessage userMessage);

    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 更新结果
     */
    int markAsRead(@Param("messageId") Long messageId,
                   @Param("userId") Long userId);

    /**
     * 批量标记为已读
     * @param userId 用户ID
     * @param messageIds 消息ID列表
     * @return 更新结果
     */
    int markAsReadBatch(@Param("userId") Long userId,
                        @Param("messageIds") List<Long> messageIds);

    /**
     * 标记所有消息为已读
     * @param userId 用户ID
     * @return 更新结果
     */
    int markAllAsRead(@Param("userId") Long userId);

    /**
     * 按类型标记为已读
     * @param userId 用户ID
     * @param messageType 消息类型
     * @return 更新结果
     */
    int markAsReadByType(@Param("userId") Long userId,
                         @Param("messageType") String messageType);

    /**
     * 删除消息（逻辑删除）
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 删除结果
     */
    int deleteMessage(@Param("messageId") Long messageId,
                      @Param("userId") Long userId);

    /**
     * 批量删除消息（逻辑删除）
     * @param userId 用户ID
     * @param messageIds 消息ID集合
     * @return 删除结果
     */
    int deleteMessageBatch(@Param("userId") Long userId,
                           @Param("messageIds") List<Long> messageIds);

    /**
     * 清空已读消息（逻辑删除）
     * @param userId 用户ID
     * @return 删除结果
     */
    int clearReadMessages(@Param("userId") Long userId);
}