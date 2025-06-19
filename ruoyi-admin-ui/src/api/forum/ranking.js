import request from '@/utils/request'

// 查询帖子排名列表
export function listRanking(query) {
  return request({
    url: '/forum/ranking/list',
    method: 'get',
    params: query
  })
}

// 查询帖子排名详细
export function getRanking(postId) {
  return request({
    url: '/forum/ranking/' + postId,
    method: 'get'
  })
}

// 新增帖子排名
export function addRanking(data) {
  return request({
    url: '/forum/ranking',
    method: 'post',
    data: data
  })
}

// 修改帖子排名
export function updateRanking(data) {
  return request({
    url: '/forum/ranking',
    method: 'put',
    data: data
  })
}

// 删除帖子排名
export function delRanking(postId) {
  return request({
    url: '/forum/ranking/' + postId,
    method: 'delete'
  })
}
