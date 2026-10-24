import request from '@/utils/request'

// 查询结算单列表
export function listShopSettlement(query) {
  return request({
    url: '/commerce/shop/settlement/list',
    method: 'get',
    params: query
  })
}

// 按账期生成结算单
export function generateShopSettlement(periodStart, periodEnd) {
  return request({
    url: '/commerce/shop/settlement/generate',
    method: 'post',
    params: { periodStart, periodEnd }
  })
}

// 标记结算单已打款
export function payShopSettlement(settlementId) {
  return request({
    url: '/commerce/shop/settlement/pay/' + settlementId,
    method: 'post'
  })
}
