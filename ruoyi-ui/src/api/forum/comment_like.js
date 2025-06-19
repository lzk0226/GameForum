import request from '@/utils/request'

// 查询评论点赞列表
export function listComment_like(query) {
  return request({
    url: '/forum/comment_like/list',
    method: 'get',
    params: query
  })
}

// 查询评论点赞详细
export function getComment_like(userId) {
  return request({
    url: '/forum/comment_like/' + userId,
    method: 'get'
  })
}

// 新增评论点赞
export function addComment_like(data) {
  return request({
    url: '/forum/comment_like',
    method: 'post',
    data: data
  })
}

// 修改评论点赞
export function updateComment_like(data) {
  return request({
    url: '/forum/comment_like',
    method: 'put',
    data: data
  })
}

// 删除评论点赞
export function delComment_like(userId) {
  return request({
    url: '/forum/comment_like/' + userId,
    method: 'delete'
  })
}
