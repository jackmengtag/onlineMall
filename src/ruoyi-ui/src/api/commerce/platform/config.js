import request from '@/utils/request'

// 查询平台配置列表
export function listPlatformConfig(query) {
  return request({
    url: '/commerce/platform/config/list',
    method: 'get',
    params: query
  })
}

// 批量修改平台配置
export function updatePlatformConfig(data) {
  return request({
    url: '/commerce/platform/config',
    method: 'put',
    data: data
  })
}
