import request from '@/api/request'

export function add(data) {
  return request({
    url: '/demo',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/demo',
    method: 'put',
    data
  })
}

export function del(ids) {
  return request({
    url: '/demo',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }
