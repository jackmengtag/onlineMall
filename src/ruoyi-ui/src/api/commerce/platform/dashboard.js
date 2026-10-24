import request from '@/utils/request'

// 查询平台驾驶舱汇总数据
export function getPlatformSummary() {
  return request({
    url: '/commerce/platform/dashboard/summary',
    method: 'get'
  })
}
