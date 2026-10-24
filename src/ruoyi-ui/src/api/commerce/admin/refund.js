import request from '@/utils/request'

// 查询退款单列表
export function listRefund(query) {
  return request({
    url: '/commerce/admin/refund/list',
    method: 'get',
    params: query
  })
}

// 查询退款单详细
export function getRefund(refundId) {
  return request({
    url: '/commerce/admin/refund/' + refundId,
    method: 'get'
  })
}

// 审核通过退款
export function approveRefund(refundId) {
  return request({
    url: '/commerce/admin/refund/approve/' + refundId,
    method: 'post'
  })
}

// 驳回退款
export function rejectRefund(refundId, reason) {
  return request({
    url: '/commerce/admin/refund/reject/' + refundId,
    method: 'post',
    params: { reason: reason }
  })
}

// 完成退款
export function completeRefund(refundId) {
  return request({
    url: '/commerce/admin/refund/complete/' + refundId,
    method: 'post'
  })
}
