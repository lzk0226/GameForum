import request from '@/utils/request'

// 查询CDK商品分类列表
export function listCategory(query) {
  return request({
    url: '/purchase/category/list',
    method: 'get',
    params: query
  })
}

// 查询CDK商品分类详细
export function getCategory(categoryId) {
  return request({
    url: '/purchase/category/' + categoryId,
    method: 'get'
  })
}

// 新增CDK商品分类
export function addCategory(data) {
  return request({
    url: '/purchase/category',
    method: 'post',
    data: data
  })
}

// 修改CDK商品分类
export function updateCategory(data) {
  return request({
    url: '/purchase/category',
    method: 'put',
    data: data
  })
}

// 删除CDK商品分类
export function delCategory(categoryId) {
  return request({
    url: '/purchase/category/' + categoryId,
    method: 'delete'
  })
}
