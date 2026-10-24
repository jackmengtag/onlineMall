import request from '@/utils/request'

// 查询店铺资料
export function getShopProfile() {
  return request({
    url: '/commerce/shop/profile',
    method: 'get'
  })
}

// 修改店铺资料
export function updateShopProfile(data) {
  return request({
    url: '/commerce/shop/profile',
    method: 'put',
    data: data
  })
}
