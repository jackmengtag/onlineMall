import request from '@/utils/request'

// 查询店铺订单列表
export function listShopOrder(query) {
  return request({
    url: '/commerce/shop/order/list',
    method: 'get',
    params: query
  })
}

// 查询店铺订单详情
export function getShopOrder(orderId) {
  return request({
    url: '/commerce/shop/order/' + orderId,
    method: 'get'
  })
}

// 取消店铺订单
export function cancelShopOrder(orderId, reason) {
  return request({
    url: '/commerce/shop/order/cancel/' + orderId,
    method: 'post',
    params: { reason }
  })
}
