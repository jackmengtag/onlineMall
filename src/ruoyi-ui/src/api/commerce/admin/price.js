import request from '@/utils/request'

// 查询价格列表
export function listPrice(query) {
  return request({
    url: '/commerce/admin/price/list',
    method: 'get',
    params: query
  })
}

// 查询价格详情
export function getPrice(priceId) {
  return request({
    url: '/commerce/admin/price/' + priceId,
    method: 'get'
  })
}

// 新增价格
export function addPrice(data) {
  return request({
    url: '/commerce/admin/price',
    method: 'post',
    data: data
  })
}

// 修改价格
export function updatePrice(data) {
  return request({
    url: '/commerce/admin/price',
    method: 'put',
    data: data
  })
}

// 删除价格
export function delPrice(priceIds) {
  return request({
    url: '/commerce/admin/price/' + priceIds,
    method: 'delete'
  })
}

// 解析 SKU 生效价格
export function resolvePrice(query) {
  return request({
    url: '/commerce/admin/price/resolve',
    method: 'get',
    params: query
  })
}
