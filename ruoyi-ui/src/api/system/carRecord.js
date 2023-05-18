import request from '@/utils/request'

// 查询计时记录列表
export function listCarRecord(query) {
  return request({
    url: '/system/carRecord/list',
    method: 'get',
    params: query
  })
}

// 查询计时记录详细
export function getCarRecord(id) {
  return request({
    url: '/system/carRecord/' + id,
    method: 'get'
  })
}

// 新增计时记录
export function addCarRecord(data) {
  return request({
    url: '/system/carRecord',
    method: 'post',
    data: data
  })
}

// 修改计时记录
export function updateCarRecord(data) {
  return request({
    url: '/system/carRecord',
    method: 'put',
    data: data
  })
}

// 删除计时记录
export function delCarRecord(id) {
  return request({
    url: '/system/carRecord/' + id,
    method: 'delete'
  })
}
