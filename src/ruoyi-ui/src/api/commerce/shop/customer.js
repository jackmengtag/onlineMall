import request from '@/utils/request'

// 查询店铺客户列表
export function listShopCustomer(query) {
  return request({
    url: '/commerce/shop/customer/list',
    method: 'get',
    params: query
  })
}
