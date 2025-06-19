import request from '@/utils/request'

// 查询CDK订单项列表
export function listItem(query) {
  return request({
    url: '/purchase/item/list',
    method: 'get',
    params: query
  })
}

// 查询CDK订单项详细
export function getItem(itemId) {
  return request({
    url: '/purchase/item/' + itemId,
    method: 'get'
  })
}

// 新增CDK订单项
export function addItem(data) {
  return request({
    url: '/purchase/item',
    method: 'post',
    data: data
  })
}

// 修改CDK订单项
export function updateItem(data) {
  return request({
    url: '/purchase/item',
    method: 'put',
    data: data
  })
}

// 删除CDK订单项
export function delItem(itemId) {
  return request({
    url: '/purchase/item/' + itemId,
    method: 'delete'
  })
}
