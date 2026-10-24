import request from '@/utils/request'

// 查询订单列表
export function listOrder(query) {
  return request({
    url: '/commerce/admin/order/list',
    method: 'get',
    params: query
  })
}

// 查询订单详情
export function getOrder(orderId) {
  return request({
    url: '/commerce/admin/order/' + orderId,
    method: 'get'
  })
}

// 创建订单
export function addOrder(data) {
  return request({
    url: '/commerce/admin/order',
    method: 'post',
    data: data
  })
}

// 取消订单
export function cancelOrder(orderId, reason) {
  return request({
    url: '/commerce/admin/order/cancel/' + orderId,
    method: 'post',
    params: { reason: reason }
  })
}

// 完成订单
export function completeOrder(orderId) {
  return request({
    url: '/commerce/admin/order/complete/' + orderId,
    method: 'post'
  })
}

// 查询订单状态历史
export function listOrderHistory(orderId) {
  return request({
    url: '/commerce/admin/order/history/' + orderId,
    method: 'get'
  })
}

// 接入渠道订单
export function addExternalOrder(data) {
  return request({
    url: '/commerce/admin/order/external',
    method: 'post',
    data: data
  })
}
