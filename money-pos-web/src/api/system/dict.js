import request from '@/api/request'

export function add (data) {
  return request({
    url: '/dict',
    method: 'post',
    data
  })
}

export function edit (data) {
  return request({
    url: '/dict',
    method: 'put',
    data
  })
}

export function del (ids) {
  return request({
    url: '/dict',
    method: 'delete',
    data: ids
  })
}

export function getDetail (dict) {
  return request({
    url: `/dict/detail?dict=${dict}`,
    method: 'get'
  })
}

export default { getDetail, add, edit, del }

