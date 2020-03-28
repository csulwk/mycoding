import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/vat-web/table/list',
    method: 'get',
    params
  })
}
