import request from '@/utils/request'

// 查询渠道配置列表
export function listPlatformChannel(query) {
  return request({
    url: '/commerce/platform/channel/list',
    method: 'get',
    params: query
  })
}

// 查询渠道配置详细
export function getPlatformChannel(channelConfigId) {
  return request({
    url: '/commerce/platform/channel/' + channelConfigId,
    method: 'get'
  })
}

// 新增渠道配置
export function addPlatformChannel(data) {
  return request({
    url: '/commerce/platform/channel',
    method: 'post',
    data: data
  })
}

// 修改渠道配置
export function updatePlatformChannel(data) {
  return request({
    url: '/commerce/platform/channel',
    method: 'put',
    data: data
  })
}

// 删除渠道配置
export function delPlatformChannel(channelConfigIds) {
  return request({
    url: '/commerce/platform/channel/' + channelConfigIds,
    method: 'delete'
  })
}
