import request from '@/api/request'

export function updateProfile(data) {
  return request({
    url: '/users/updateProfile',
    method: 'post',
    data
  })
}

export function changePassword(data) {
  return request({
    url: '/users/changePassword',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/users',
    method: 'put',
    data
  })
}

export function del(ids) {
  return request({
    url: '/users',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }
