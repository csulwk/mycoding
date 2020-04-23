import request from '@/utils/request'

export function getAllRole() {
  return request({
    url: '/auth/role/info',
    method: 'get'
  })
}

export function getRolePermList(role) {
  return request({
    url: `role/perms/${role}`,
    method: 'get'
  })
}
