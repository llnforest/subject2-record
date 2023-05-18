import request from '@/utils/request'

// 查询车辆管理列表
export function listCar(query) {
  return request({
    url: '/system/car/list',
    method: 'get',
    params: query
  })
}

// 查询车辆管理详细
export function getCar(id) {
  return request({
    url: '/system/car/' + id,
    method: 'get'
  })
}

// 新增车辆管理
export function addCar(data) {
  return request({
    url: '/system/car',
    method: 'post',
    data: data
  })
}

// 修改车辆管理
export function updateCar(data) {
  return request({
    url: '/system/car',
    method: 'put',
    data: data
  })
}

// 删除车辆管理
export function delCar(id) {
  return request({
    url: '/system/car/' + id,
    method: 'delete'
  })
}

// 用户状态修改
export function changeCarStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/system/car/changeStatus',
    method: 'put',
    data
  })
}
// 查询车辆列表
export function carList(params) {
  return request({
    url: '/system/car/carList',
    method: 'get',
    params
  })
}
