import request from '@/api/request'

export function add(data) {
  return request({
    url: '/ums/member',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/ums/member',
    method: 'put',
    data
  })
}

export function del(ids) {
  return request({
    url: '/ums/member',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }

