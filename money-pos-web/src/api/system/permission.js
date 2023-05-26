import request from '@/api/request'

export function getPermissionsLazy(pid) {
  return request({
    url: `/permissions/lazy?parentId=${pid}`,
    method: 'get'
  })
}

export function getAllSubIds(id) {
  return request({
    url: `/permissions/allSubIds?id=${id}`,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/permissions',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/permissions',
    method: 'put',
    data
  })
}

export function del(ids) {
  return request({
    url: '/permissions',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del, getPermissionsLazy }
