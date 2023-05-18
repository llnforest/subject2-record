import request from '@/utils/request'

// 查询计时统计列表
export function listCarAnalyse(query) {
  return request({
    url: '/system/carAnalyse/list',
    method: 'get',
    params: query
  })
}


