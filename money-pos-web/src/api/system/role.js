import request from '@/api/request'

export function getAll() {
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

export function changeStatus(id, enabled) {
  return request({
    url: `/roles/changeStatus?id=${id}&enabled=${enabled}`,
    method: 'post'
  })
}

export function configurePermissions(id, permissionIds) {
  return request({
    url: `/roles/${id}/permission`,
    method: 'post',
    data: permissionIds
  })
}

export default { add, edit, del, changeStatus, configurePermissions }
