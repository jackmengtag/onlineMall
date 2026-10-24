import request from '@/utils/request'

// 查询平台用户与店铺绑定列表
export function listPlatformUser(query) {
  return request({
    url: '/commerce/platform/user/list',
    method: 'get',
    params: query
  })
}
