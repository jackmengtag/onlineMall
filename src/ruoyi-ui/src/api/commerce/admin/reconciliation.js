import request from '@/utils/request'

// 查询对账汇总
export function getReconciliationSummary(query) {
  return request({
    url: '/commerce/admin/reconciliation/summary',
    method: 'get',
    params: query
  })
}

// 查询对账明细
export function listReconciliation(query) {
  return request({
    url: '/commerce/admin/reconciliation/list',
    method: 'get',
    params: query
  })
}
