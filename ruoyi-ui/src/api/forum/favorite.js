import request from '@/utils/request'

// 查询帖子收藏列表
export function listFavorite(query) {
  return request({
    url: '/forum/favorite/list',
    method: 'get',
    params: query
  })
}

// 查询帖子收藏详细
export function getFavorite(userId) {
  return request({
    url: '/forum/favorite/' + userId,
    method: 'get'
  })
}

// 新增帖子收藏
export function addFavorite(data) {
  return request({
    url: '/forum/favorite',
    method: 'post',
    data: data
  })
}

// 修改帖子收藏
export function updateFavorite(data) {
  return request({
    url: '/forum/favorite',
    method: 'put',
    data: data
  })
}

// 删除帖子收藏
export function delFavorite(userId) {
  return request({
    url: '/forum/favorite/' + userId,
    method: 'delete'
  })
}
