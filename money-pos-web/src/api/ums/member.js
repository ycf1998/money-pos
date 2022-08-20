import request from '@/api/request'

export function add(data) {
  return request({
    url: '/members',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/members',
    method: 'put',
    data
  })
}

export function del(ids) {
  return request({
    url: '/members',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }

