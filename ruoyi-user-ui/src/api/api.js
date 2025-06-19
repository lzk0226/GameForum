// src/api/api.js
import axios from 'axios';

// 创建axios实例
const request = axios.create({
    baseURL: '/api', // API的base_url
    timeout: 5000 // 请求超时时间
});

// 请求拦截器
request.interceptors.request.use(
    config => {
        // 可以在这里添加token等认证信息
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config;
    },
    error => {
        console.error(error);
        return Promise.reject(error);
    }
);

// 响应拦截器
request.interceptors.response.use(
    response => {
        const res = response.data;
        // 根据后端接口约定判断请求是否成功
        if (res.code !== 200 && res.code !== undefined) {
            // 处理各种错误情况
            console.error('请求失败:', res.msg || '未知错误');
            return Promise.reject(new Error(res.msg || '未知错误'));
        } else {
            return res;
        }
    },
    error => {
        console.error('请求错误:', error);
        return Promise.reject(error);
    }
);

// 论坛模块API
export const forumAPI = {
    // 帖子相关
    post: {
        // 获取帖子列表
        getList(query) {
            return request({
                url: '/forum/post/list',
                method: 'get',
                params: query
            });
        },
        // 获取帖子详情
        getDetail(postId) {
            return request({
                url: `/forum/post/${postId}`,
                method: 'get'
            });
        },
        // 新增帖子
        add(data) {
            return request({
                url: '/forum/post',
                method: 'post',
                data
            });
        },
        // 修改帖子
        update(data) {
            return request({
                url: '/forum/post',
                method: 'put',
                data
            });
        },
        // 删除帖子
        delete(postIds) {
            return request({
                url: `/forum/post/${postIds}`,
                method: 'delete'
            });
        }
    },

    // 评论相关
    comment: {
        // 获取评论列表
        getList(query) {
            return request({
                url: '/forum/comment/list',
                method: 'get',
                params: query
            });
        },
        // 获取评论详情
        getDetail(commentId) {
            return request({
                url: `/forum/comment/${commentId}`,
                method: 'get'
            });
        },
        // 添加评论
        add(data) {
            return request({
                url: '/forum/comment',
                method: 'post',
                data
            });
        },
        // 修改评论
        update(data) {
            return request({
                url: '/forum/comment',
                method: 'put',
                data
            });
        },
        // 删除评论
        delete(commentIds) {
            return request({
                url: `/forum/comment/${commentIds}`,
                method: 'delete'
            });
        }
    },

    // 版块相关
    section: {
        // 获取版块列表
        getList(query) {
            return request({
                url: '/forum/section/list',
                method: 'get',
                params: query
            });
        },
        // 获取版块详情
        getDetail(sectionId) {
            return request({
                url: `/forum/section/${sectionId}`,
                method: 'get'
            });
        }
    },

    // 游戏相关
    game: {
        // 获取游戏列表
        getList(query) {
            return request({
                url: '/forum/game/list',
                method: 'get',
                params: query
            });
        },
        // 获取游戏详情
        getDetail(gameId) {
            return request({
                url: `/forum/game/${gameId}`,
                method: 'get'
            });
        }
    },

    // 游戏类型相关
    gameType: {
        // 获取游戏类型列表
        getList(query) {
            return request({
                url: '/forum/type/list',
                method: 'get',
                params: query
            });
        }
    },

    // 帖子点赞相关
    postLike: {
        // 添加点赞
        add(data) {
            return request({
                url: '/forum/post_like',
                method: 'post',
                data
            });
        },
        // 取消点赞
        delete(userIds) {
            return request({
                url: `/forum/post_like/${userIds}`,
                method: 'delete'
            });
        }
    },

    // 评论点赞相关
    commentLike: {
        // 添加点赞
        add(data) {
            return request({
                url: '/forum/comment_like',
                method: 'post',
                data
            });
        },
        // 取消点赞
        delete(userIds) {
            return request({
                url: `/forum/comment_like/${userIds}`,
                method: 'delete'
            });
        }
    },

    // 帖子收藏相关
    favorite: {
        // 添加收藏
        add(data) {
            return request({
                url: '/forum/favorite',
                method: 'post',
                data
            });
        },
        // 取消收藏
        delete(userIds) {
            return request({
                url: `/forum/favorite/${userIds}`,
                method: 'delete'
            });
        },
        // 获取收藏列表
        getList(query) {
            return request({
                url: '/forum/favorite/list',
                method: 'get',
                params: query
            });
        }
    },

    // 帖子排名相关
    ranking: {
        // 获取排名列表
        getList(query) {
            return request({
                url: '/forum/ranking/list',
                method: 'get',
                params: query
            });
        }
    }
};

export default {
    forumAPI
};