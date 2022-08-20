import request from '@/api/request'

export function add(data) {
  return request({
    url: '/dict/detail',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/dict/detail',
    method: 'put',
    data
  })
}

export function del(ids) {
  return request({
    url: '/dict/detail',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }

