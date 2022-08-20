import request from '@/api/request'

export function add(data) {
  const formData = new FormData()
  formData.append('goods', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }))
  formData.append('pic', data.picFile)
  return request({
    url: '/goods',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/mixed' }
  })
}

export function edit(data) {
  const formData = new FormData()
  formData.append('goods', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }))
  formData.append('pic', data.picFile)
  return request({
    url: '/goods',
    method: 'put',
    data: formData,
    headers: { 'Content-Type': 'multipart/mixed' }
  })
}

export function del(ids) {
  return request({
    url: '/goods',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }

