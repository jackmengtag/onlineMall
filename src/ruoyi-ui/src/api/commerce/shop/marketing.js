import request from '@/utils/request'

// 查询促销活动列表
export function listShopPromotion(query) {
  return request({
    url: '/commerce/shop/marketing/promotion/list',
    method: 'get',
    params: query
  })
}

// 新增促销活动
export function addShopPromotion(data) {
  return request({
    url: '/commerce/shop/marketing/promotion',
    method: 'post',
    data: data
  })
}

// 修改促销活动
export function updateShopPromotion(data) {
  return request({
    url: '/commerce/shop/marketing/promotion',
    method: 'put',
    data: data
  })
}

// 删除促销活动
export function delShopPromotion(promotionIds) {
  return request({
    url: '/commerce/shop/marketing/promotion/' + promotionIds,
    method: 'delete'
  })
}

// 查询优惠券列表
export function listShopCoupon(query) {
  return request({
    url: '/commerce/shop/marketing/coupon/list',
    method: 'get',
    params: query
  })
}
