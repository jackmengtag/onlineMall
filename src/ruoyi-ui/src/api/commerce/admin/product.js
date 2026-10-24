import request from '@/utils/request'

// 查询 SPU 列表
export function listProduct(query) {
  return request({
    url: '/commerce/admin/product/list',
    method: 'get',
    params: query
  })
}

// 查询 SPU 详情（含 SKU 列表）
export function getProduct(spuId) {
  return request({
    url: '/commerce/admin/product/' + spuId,
    method: 'get'
  })
}

// 新增 SPU
export function addProduct(data) {
  return request({
    url: '/commerce/admin/product',
    method: 'post',
    data: data
  })
}

// 修改 SPU
export function updateProduct(data) {
  return request({
    url: '/commerce/admin/product',
    method: 'put',
    data: data
  })
}

// 删除 SPU
export function delProduct(spuIds) {
  return request({
    url: '/commerce/admin/product/' + spuIds,
    method: 'delete'
  })
}

// 修改 SPU 状态
export function changeProductStatus(spuId, status) {
  return request({
    url: '/commerce/admin/product/status',
    method: 'post',
    params: { spuId: spuId, status: status }
  })
}

// 查询 SKU 列表
export function listSku(query) {
  return request({
    url: '/commerce/admin/product/sku/list',
    method: 'get',
    params: query
  })
}

// 查询 SKU 详情
export function getSku(skuId) {
  return request({
    url: '/commerce/admin/product/sku/' + skuId,
    method: 'get'
  })
}

// 新增 SKU
export function addSku(data) {
  return request({
    url: '/commerce/admin/product/sku',
    method: 'post',
    data: data
  })
}

// 修改 SKU
export function updateSku(data) {
  return request({
    url: '/commerce/admin/product/sku',
    method: 'put',
    data: data
  })
}

// 删除 SKU
export function delSku(skuIds) {
  return request({
    url: '/commerce/admin/product/sku/' + skuIds,
    method: 'delete'
  })
}

// 查询 SKU 可售情况
export function getAvailability(query) {
  return request({
    url: '/commerce/admin/product/availability',
    method: 'get',
    params: query
  })
}

// 查询 SKU 下拉选项
export function listSkuOptions(query) {
  return request({
    url: '/commerce/admin/product/skuOptions',
    method: 'get',
    params: query
  })
}
