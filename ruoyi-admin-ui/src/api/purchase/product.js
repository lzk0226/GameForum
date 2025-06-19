import request from '@/utils/request'

// 查询CDK商品列表
export function listProduct(query) {
  return request({
    url: '/purchase/product/list',
    method: 'get',
    params: query
  })
}

// 查询CDK商品详细
export function getProduct(productId) {
  return request({
    url: '/purchase/product/' + productId,
    method: 'get'
  })
}

// 新增CDK商品
export function addProduct(data) {
  return request({
    url: '/purchase/product',
    method: 'post',
    data: data
  })
}

// 修改CDK商品
export function updateProduct(data) {
  return request({
    url: '/purchase/product',
    method: 'put',
    data: data
  })
}

// 删除CDK商品
export function delProduct(productId) {
  return request({
    url: '/purchase/product/' + productId,
    method: 'delete'
  })
}
