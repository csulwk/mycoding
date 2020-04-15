import request from '@/utils/request'

export function getAllUser() {
  return request({
    url: '/auth/user/info',
    method: 'get'
  })
}

export function getUserRoles() {
  return request({
    url: 'user/roles',
    method: 'get'
  })
}

export function addUser(user) {
  return request({
    url: 'user/add',
    method: 'post',
    data: user
  })
}

export function updateUser(user) {
  return request({
    url: 'user/update',
    method: 'put',
    data: user
  })
}

export function deleteUser(username) {
  return request({
    url: `user/delete/${username}`,
    method: 'delete'
  })
}
