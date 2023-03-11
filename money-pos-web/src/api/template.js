import request from '@/api/request'
export function add(data) {
  return request({
    url: '/template',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/template',
    method: 'put',
    data
  })
}

export function del(ids) {
  return request({
    url: '/template',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }
