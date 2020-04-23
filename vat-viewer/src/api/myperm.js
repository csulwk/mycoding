import request from '@/utils/request'

export function getAllPerm() {
  return request({
    url: '/auth/perm/info',
    method: 'get'
  })
}

export function getAllPermList() {
  return request({
    url: `perm/all`,
    method: 'get'
  })
}

