import request from '@/utils/request'

// 查询店铺经营报表
export function getShopReport(params) {
  return request({
    url: '/commerce/shop/report/summary',
    method: 'get',
    params: params
  })
}
