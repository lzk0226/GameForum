import request from '@/utils/request'

// 查询公告栏列表
export function listAnnouncement(query) {
  return request({
    url: '/forum/announcement/list',
    method: 'get',
    params: query
  })
}

// 查询公告栏详细
export function getAnnouncement(titel) {
  return request({
    url: '/forum/announcement/' + titel,
    method: 'get'
  })
}

// 新增公告栏
export function addAnnouncement(data) {
  return request({
    url: '/forum/announcement',
    method: 'post',
    data: data
  })
}

// 修改公告栏
export function updateAnnouncement(data) {
  return request({
    url: '/forum/announcement',
    method: 'put',
    data: data
  })
}

// 删除公告栏
export function delAnnouncement(titel) {
  return request({
    url: '/forum/announcement/' + titel,
    method: 'delete'
  })
}
