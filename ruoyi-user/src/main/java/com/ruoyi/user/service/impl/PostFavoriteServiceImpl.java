package com.ruoyi.user.service.impl;

import com.ruoyi.user.domain.PostFavorite;
import com.ruoyi.user.domain.User;
import com.ruoyi.user.domain.UserMessage;
import com.ruoyi.user.domain.Post;
import com.ruoyi.user.mapper.PostFavoriteMapper;
import com.ruoyi.user.mapper.PostMapper;
import com.ruoyi.user.mapper.UserMapper;
import com.ruoyi.user.mapper.UserMessageMapper;
import com.ruoyi.user.service.IPostFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 帖子收藏Service业务层处理
 *
 * @author SockLightDust
 * @date 2025-10-19
 */
@Service
public class PostFavoriteServiceImpl implements IPostFavoriteService {

    @Autowired
    private PostFavoriteMapper postFavoriteMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public List<PostFavorite> selectFavoriteListByUserId(Long userId) {
        return postFavoriteMapper.selectFavoriteListByUserId(userId);
    }

    @Override
    public List<PostFavorite> selectFavoriteListByPostId(Integer postId) {
        return postFavoriteMapper.selectFavoriteListByPostId(postId);
    }

    @Override
    public boolean checkUserFavorited(Long userId, Integer postId) {
        return postFavoriteMapper.checkUserFavorited(userId, postId) > 0;
    }

    @Override
    @Transactional
    public int addFavorite(Long userId, Integer postId) {
        // 检查是否已收藏
        if (checkUserFavorited(userId, postId)) {
            return 0;
        }

        // 添加收藏记录
        int result = postFavoriteMapper.insertFavorite(userId, postId);

        // 发送消息通知
        if (result > 0) {
            sendFavoriteMessage(userId, postId);
        }

        return result;
    }

    @Override
    @Transactional
    public int removeFavorite(Long userId, Integer postId) {
        return postFavoriteMapper.deleteFavorite(userId, postId);
    }

    @Override
    @Transactional
    public int removeFavoriteByIds(Long userId, List<Integer> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return 0;
        }
        return postFavoriteMapper.deleteFavoriteByIds(userId, postIds);
    }

    @Override
    public int countFavoriteByUserId(Long userId) {
        return postFavoriteMapper.countFavoriteByUserId(userId);
    }

    @Override
    public int countFavoriteByPostId(Integer postId) {
        return postFavoriteMapper.countFavoriteByPostId(postId);
    }

    @Override
    public Map<Integer, Boolean> checkUserFavoritedBatch(Long userId, List<Integer> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return new HashMap<>();
        }

        List<HashMap<String, Object>> result = postFavoriteMapper.checkUserFavoritedBatch(userId, postIds);

        return result.stream()
                .collect(Collectors.toMap(
                        map -> (Integer) map.get("post_id"),
                        map -> true
                ));
    }

    /**
     * 发送收藏消息通知
     */
    private void sendFavoriteMessage(Long userId, Integer postId) {
        try {
            // 获取帖子信息
            Post post = postMapper.selectPostById(postId);
            if (post == null || post.getUserId().equals(userId)) {
                return; // 帖子不存在或自己收藏自己的帖子
            }

            // 获取用户信息
            User user = userMapper.selectUserById(userId);
            if (user == null) {
                return;
            }

            // 创建消息
            UserMessage message = new UserMessage();
            message.setReceiverId(post.getUserId());
            message.setSenderId(userId);
            message.setMessageType("3"); // 收藏类型
            message.setMessageContent(user.getNickName() + " 收藏了你的帖子");
            message.setRelatedType("0"); // 关联帖子
            message.setRelatedId(postId);
            message.setIsRead("0");
            message.setDelFlag("0");

            userMessageMapper.insertMessage(message);
        } catch (Exception e) {
            // 消息发送失败不影响主流程
            e.printStackTrace();
        }
    }
}
