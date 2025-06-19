import request from '@/utils/request'

// 查询商品分类关联列表
export function listProductcategory(query) {
  return request({
    url: '/purchase/productcategory/list',
    method: 'get',
    params: query
  })
}

// 查询商品分类关联详细
export function getProductcategory(productId) {
  return request({
    url: '/purchase/productcategory/' + productId,
    method: 'get'
  })
}

// 新增商品分类关联
export function addProductcategory(data) {
  return request({
    url: '/purchase/productcategory',
    method: 'post',
    data: data
  })
}

// 修改商品分类关联
export function updateProductcategory(data) {
  return request({
    url: '/purchase/productcategory',
    method: 'put',
    data: data
  })
}

// 删除商品分类关联
export function delProductcategory(productId) {
  return request({
    url: '/purchase/productcategory/' + productId,
    method: 'delete'
  })
}
