// 这是一个建议的API文件，用于处理游戏类型相关请求
import request from '@/utils/request'

// 查询游戏类型列表
export function listGameType(query) {
  return request({
    url: '/forum/type/list',
    method: 'get',
    params: query
  })
}

// 查询游戏类型详细
export function getGameType(typeId) {
  return request({
    url: '/forum/type/' + typeId,
    method: 'get'
  })
}

// 新增游戏类型
export function addGameType(data) {
  return request({
    url: '/forum/type',
    method: 'post',
    data: data
  })
}

// 修改游戏类型
export function updateGameType(data) {
  return request({
    url: '/forum/type',
    method: 'put',
    data: data
  })
}

// 删除游戏类型
export function delGameType(typeId) {
  return request({
    url: '/forum/type/' + typeId,
    method: 'delete'
  })
}
