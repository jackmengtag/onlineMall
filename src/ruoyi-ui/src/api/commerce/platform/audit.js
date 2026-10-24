import request from '@/utils/request'

// 查询平台审计日志列表
export function listPlatformAudit(query) {
  return request({
    url: '/commerce/platform/audit/list',
    method: 'get',
    params: query
  })
}
