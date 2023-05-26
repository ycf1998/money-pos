import request from '@/api/request'

export function getAllRole() {
  return request({
    url: '/roles/all',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/roles',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/roles',
    method: 'put',
    data
  })
}

export function del(ids) {
  return request({
    url: '/roles',
    method: 'delete',
    data: ids
  })
}

export function configurePermissions(id, permissionIds) {
  return request({
    url: `/roles/${id}/permission`,
    method: 'post',
    data: permissionIds
  })
}

export default { add, edit, del, configurePermissions }
