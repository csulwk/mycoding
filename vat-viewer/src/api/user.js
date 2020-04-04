import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/mc/admin/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/mc/admin/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/mc/admin/logout',
    method: 'post'
  })
}
