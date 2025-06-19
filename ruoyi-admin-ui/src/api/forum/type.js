import request from '@/utils/request'

// 查询游戏类型列表
export function listType(query) {
  return request({
    url: '/forum/type/list',
    method: 'get',
    params: query
  })
}

// 查询游戏类型详细
export function getType(typeId) {
  return request({
    url: '/forum/type/' + typeId,
    method: 'get'
  })
}

// 新增游戏类型
export function addType(data) {
  return request({
    url: '/forum/type',
    method: 'post',
    data: data
  })
}

// 修改游戏类型
export function updateType(data) {
  return request({
    url: '/forum/type',
    method: 'put',
    data: data
  })
}

// 删除游戏类型
export function delType(typeId) {
  return request({
    url: '/forum/type/' + typeId,
    method: 'delete'
  })
}
