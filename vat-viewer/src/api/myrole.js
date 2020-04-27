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

export function addRole(role) {
  return request({
    url: 'role/add',
    method: 'post',
    data: role
  })
}

export function updateRole(role) {
  return request({
    url: 'role/update',
    method: 'put',
    data: role
  })
}

export function deleteRole(roleCode) {
  return request({
    url: `role/delete/${roleCode}`,
    method: 'delete'
  })
}

export function getUsersOfRole(roleId) {
  return request({
    url: `role/${roleId}/users`,
    method: 'get'
  })
}

export function getPermsOfRole(roleId) {
  return request({
    url: `role/${roleId}/perms`,
    method: 'get'
  })
}
