import request from '@/utils/request'

// 查询CDK库存列表
export function listStock(query) {
  return request({
    url: '/purchase/stock/list',
    method: 'get',
    params: query
  })
}

// 查询CDK库存详细
export function getStock(stockId) {
  return request({
    url: '/purchase/stock/' + stockId,
    method: 'get'
  })
}

// 新增CDK库存
export function addStock(data) {
  return request({
    url: '/purchase/stock',
    method: 'post',
    data: data
  })
}

// 修改CDK库存
export function updateStock(data) {
  return request({
    url: '/purchase/stock',
    method: 'put',
    data: data
  })
}

// 删除CDK库存
export function delStock(stockId) {
  return request({
    url: '/purchase/stock/' + stockId,
    method: 'delete'
  })
}
