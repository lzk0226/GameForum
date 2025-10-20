import request from '@/utils/request'

// 查询用户消息列表
export function listMessage(query) {
  return request({
    url: '/forum/message/list',
    method: 'get',
    params: query
  })
}

// 查询用户消息详细
export function getMessage(messageId) {
  return request({
    url: '/forum/message/' + messageId,
    method: 'get'
  })
}

// 新增用户消息
export function addMessage(data) {
  return request({
    url: '/forum/message',
    method: 'post',
    data: data
  })
}

// 修改用户消息
export function updateMessage(data) {
  return request({
    url: '/forum/message',
    method: 'put',
    data: data
  })
}

// 删除用户消息
export function delMessage(messageId) {
  return request({
    url: '/forum/message/' + messageId,
    method: 'delete'
  })
}
