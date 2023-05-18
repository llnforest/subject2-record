import request from '@/utils/request'

// 查询计时操作列表
export function listCarData(query) {
  return request({
    url: '/system/carData/list',
    method: 'get',
    params: query
  })
}

// 查询计时操作详细
export function getCarData(id) {
  return request({
    url: '/system/carData/' + id,
    method: 'get'
  })
}

// 新增计时操作
export function addCarData(data) {
  return request({
    url: '/system/carData',
    method: 'post',
    data: data
  })
}

// 修改计时操作
export function updateCarData(data) {
  return request({
    url: '/system/carData',
    method: 'put',
    data: data
  })
}

// 删除计时操作
export function delCarData(id) {
  return request({
    url: '/system/carData/' + id,
    method: 'delete'
  })
}

// 增加1人次操作
export function addOneTime(id) {
  return request({
    url: '/system/carData/addOneTime/' + id,
    method: 'put'
  })
}

// 撤销1人次操作
export function delOneTime(id) {
  return request({
    url: '/system/carData/delOneTime/' + id,
    method: 'put'
  })
}

// 撤销1人次操作
export function finishCar(id) {
  return request({
    url: '/system/carData/finishCar/' + id,
    method: 'put'
  })
}
