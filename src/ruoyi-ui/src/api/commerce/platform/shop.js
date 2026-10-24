import request from '@/utils/request'

// 查询平台店铺列表
export function listPlatformShop(query) {
  return request({
    url: '/commerce/platform/shop/list',
    method: 'get',
    params: query
  })
}

// 查询平台店铺详细
export function getPlatformShop(shopId) {
  return request({
    url: '/commerce/platform/shop/' + shopId,
    method: 'get'
  })
}

// 新增平台店铺
export function addPlatformShop(data) {
  return request({
    url: '/commerce/platform/shop',
    method: 'post',
    data: data
  })
}

// 修改平台店铺
export function updatePlatformShop(data) {
  return request({
    url: '/commerce/platform/shop',
    method: 'put',
    data: data
  })
}

// 删除平台店铺
export function delPlatformShop(shopIds) {
  return request({
    url: '/commerce/platform/shop/' + shopIds,
    method: 'delete'
  })
}

// 修改平台店铺状态
export function changePlatformShopStatus(shopId, status) {
  return request({
    url: '/commerce/platform/shop/status',
    method: 'post',
    params: { shopId, status }
  })
}
