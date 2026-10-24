import request from '@/utils/request'

// 查询店铺运单列表
export function listShopShipment(query) {
  return request({
    url: '/commerce/shop/logistics/list',
    method: 'get',
    params: query
  })
}

// 查询店铺运单详情
export function getShopShipment(shipmentId) {
  return request({
    url: '/commerce/shop/logistics/' + shipmentId,
    method: 'get'
  })
}

// 录入物流轨迹
export function addShopTracking(data) {
  return request({
    url: '/commerce/shop/logistics/track',
    method: 'post',
    data: data
  })
}
