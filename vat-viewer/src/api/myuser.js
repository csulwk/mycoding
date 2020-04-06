import request from '@/utils/request'

export function getInfo() {
  return request({
    url: '/auth/user/info',
    method: 'get'
  })
}

export function login(data) {
  return request({
    url: '/auth/user/info',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/auth/user/info',
    method: 'post'
  })
}
