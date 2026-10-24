import request from '@/utils/request'

// 查询店铺工作台汇总
export function getShopSummary() {
  return request({
    url: '/commerce/shop/dashboard/summary',
    method: 'get'
  })
}
