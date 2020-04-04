import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/vat-viewer/table/list',
    method: 'get',
    params
  })
}
