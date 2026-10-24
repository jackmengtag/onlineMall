import request from '@/utils/request'

// 运营看板汇总
export function getAdminSummary(query) {
  return request({
    url: '/commerce/admin/dashboard/summary',
    method: 'get',
    params: query
  })
}
