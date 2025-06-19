import request from '@/utils/request'

// 查询帖子点赞列表
export function listPost_like(query) {
  return request({
    url: '/forum/post_like/list',
    method: 'get',
    params: query
  })
}

// 查询帖子点赞详细
export function getPost_like(userId) {
  return request({
    url: '/forum/post_like/' + userId,
    method: 'get'
  })
}

// 新增帖子点赞
export function addPost_like(data) {
  return request({
    url: '/forum/post_like',
    method: 'post',
    data: data
  })
}

// 修改帖子点赞
export function updatePost_like(data) {
  return request({
    url: '/forum/post_like',
    method: 'put',
    data: data
  })
}

// 删除帖子点赞
export function delPost_like(userId) {
  return request({
    url: '/forum/post_like/' + userId,
    method: 'delete'
  })
}
