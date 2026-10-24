import request from '@/utils/request'

// 查询履约单列表
export function listFulfillment(query) {
  return request({
    url: '/commerce/admin/fulfillment/list',
    method: 'get',
    params: query
  })
}

// 查询履约单详情
export function getFulfillment(fulfillmentId) {
  return request({
    url: '/commerce/admin/fulfillment/' + fulfillmentId,
    method: 'get'
  })
}

// 按订单拆分履约单
export function splitFulfillment(orderId) {
  return request({
    url: '/commerce/admin/fulfillment/split/' + orderId,
    method: 'post'
  })
}

// 变更履约状态
export function changeFulfillmentStatus(fulfillmentId, toStatus) {
  return request({
    url: '/commerce/admin/fulfillment/status',
    method: 'post',
    params: { fulfillmentId: fulfillmentId, toStatus: toStatus }
  })
}

// 履约单发货
export function shipFulfillment(fulfillmentId, carrierCode, trackingNo) {
  return request({
    url: '/commerce/admin/fulfillment/ship',
    method: 'post',
    params: { fulfillmentId: fulfillmentId, carrierCode: carrierCode, trackingNo: trackingNo }
  })
}
