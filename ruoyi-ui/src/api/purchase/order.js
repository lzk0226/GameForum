import request from '@/utils/request'

// 查询CDK订单列表
export function listOrder(query) {
  return request({
    url: '/purchase/order/list',
    method: 'get',
    params: query
  })
}

// 查询CDK订单详细
export function getOrder(orderId) {
  return request({
    url: '/purchase/order/' + orderId,
    method: 'get'
  })
}

// 新增CDK订单
export function addOrder(data) {
  return request({
    url: '/purchase/order',
    method: 'post',
    data: data
  })
}

// 修改CDK订单
export function updateOrder(data) {
  return request({
    url: '/purchase/order',
    method: 'put',
    data: data
  })
}

// 删除CDK订单
export function delOrder(orderId) {
  return request({
    url: '/purchase/order/' + orderId,
    method: 'delete'
  })
}
