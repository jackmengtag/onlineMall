import request from '@/utils/request'

// 查询库存列表
export function listInventory(query) {
  return request({
    url: '/commerce/admin/inventory/list',
    method: 'get',
    params: query
  })
}

// 查询库存详情
export function getInventory(inventoryId) {
  return request({
    url: '/commerce/admin/inventory/' + inventoryId,
    method: 'get'
  })
}

// 查询库存变动流水
export function listInventoryTx(query) {
  return request({
    url: '/commerce/admin/inventory/transactions',
    method: 'get',
    params: query
  })
}

// 手工调整库存
export function adjustInventory(data) {
  return request({
    url: '/commerce/admin/inventory/adjust',
    method: 'post',
    params: data
  })
}

// 新增库存
export function addInventory(data) {
  return request({
    url: '/commerce/admin/inventory',
    method: 'post',
    data: data
  })
}
