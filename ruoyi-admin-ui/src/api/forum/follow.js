import request from '@/utils/request'

// 查询用户关注列表
export function listFollow(query) {
  return request({
    url: '/forum/follow/list',
    method: 'get',
    params: query
  })
}

// 查询用户关注详细
export function getFollow(followerId) {
  return request({
    url: '/forum/follow/' + followerId,
    method: 'get'
  })
}

// 新增用户关注
export function addFollow(data) {
  return request({
    url: '/forum/follow',
    method: 'post',
    data: data
  })
}

// 修改用户关注
export function updateFollow(data) {
  return request({
    url: '/forum/follow',
    method: 'put',
    data: data
  })
}

// 删除用户关注
export function delFollow(followerId) {
  return request({
    url: '/forum/follow/' + followerId,
    method: 'delete'
  })
}
