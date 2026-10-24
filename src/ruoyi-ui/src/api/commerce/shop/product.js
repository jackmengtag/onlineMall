import request from '@/utils/request'

// 查询店铺商品列表
export function listShopProduct(query) {
  return request({
    url: '/commerce/shop/product/list',
    method: 'get',
    params: query
  })
}

// 查询店铺商品详情（含 SKU）
export function getShopProduct(spuId) {
  return request({
    url: '/commerce/shop/product/' + spuId,
    method: 'get'
  })
}

// 修改店铺商品上下架状态
export function changeShopProductStatus(spuId, status) {
  return request({
    url: '/commerce/shop/product/status',
    method: 'post',
    params: { spuId, status }
  })
}

// 查询店铺商品 SKU 列表
export function listShopSku(spuId) {
  return request({
    url: '/commerce/shop/product/sku/list',
    method: 'get',
    params: { spuId }
  })
}
