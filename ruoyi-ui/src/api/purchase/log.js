import request from '@/utils/request'

// 查询余额变动日志列表
export function listLog(query) {
  return request({
    url: '/purchase/log/list',
    method: 'get',
    params: query
  })
}

// 查询余额变动日志详细
export function getLog(logId) {
  return request({
    url: '/purchase/log/' + logId,
    method: 'get'
  })
}

// 新增余额变动日志
export function addLog(data) {
  return request({
    url: '/purchase/log',
    method: 'post',
    data: data
  })
}

// 修改余额变动日志
export function updateLog(data) {
  return request({
    url: '/purchase/log',
    method: 'put',
    data: data
  })
}

// 删除余额变动日志
export function delLog(logId) {
  return request({
    url: '/purchase/log/' + logId,
    method: 'delete'
  })
}
