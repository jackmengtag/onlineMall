import request from '@/utils/request'

// 查询促销活动列表
export function listPromotion(query) {
  return request({
    url: '/commerce/admin/promotion/list',
    method: 'get',
    params: query
  })
}

// 查询促销活动详细
export function getPromotion(promotionId) {
  return request({
    url: '/commerce/admin/promotion/' + promotionId,
    method: 'get'
  })
}

// 新增促销活动
export function addPromotion(data) {
  return request({
    url: '/commerce/admin/promotion',
    method: 'post',
    data: data
  })
}

// 修改促销活动
export function updatePromotion(data) {
  return request({
    url: '/commerce/admin/promotion',
    method: 'put',
    data: data
  })
}

// 删除促销活动
export function delPromotion(promotionIds) {
  return request({
    url: '/commerce/admin/promotion/' + promotionIds,
    method: 'delete'
  })
}
