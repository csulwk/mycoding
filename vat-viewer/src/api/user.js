import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/mc/user/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/mc/user/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/mc/user/logout',
    method: 'post'
  })
}
