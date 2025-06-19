import request from '@/utils/request'

// 查询论坛版块列表
export function listSection(query) {
  return request({
    url: '/forum/section/list',
    method: 'get',
    params: query
  })
}

// 查询论坛版块详细
export function getSection(sectionId) {
  return request({
    url: '/forum/section/' + sectionId,
    method: 'get'
  })
}

// 新增论坛版块
export function addSection(data) {
  return request({
    url: '/forum/section',
    method: 'post',
    data: data
  })
}

// 修改论坛版块
export function updateSection(data) {
  return request({
    url: '/forum/section',
    method: 'put',
    data: data
  })
}

// 删除论坛版块
export function delSection(sectionId) {
  return request({
    url: '/forum/section/' + sectionId,
    method: 'delete'
  })
}
