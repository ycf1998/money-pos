import request from '@/api/request'

export function selectBrand() {
  return request({
    url: '/brands/select',
    method: 'get'
  })
}

export function add(data) {
  const formData = new FormData()
  formData.append('brand', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }))
  formData.append('logo', data.logoFile)
  return request({
    url: '/brands',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/mixed' }
  })
}

export function edit(data) {
  const formData = new FormData()
  formData.append('brand', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }))
  formData.append('logo', data.logoFile)
  return request({
    url: '/brands',
    method: 'put',
    data: formData,
    headers: { 'Content-Type': 'multipart/mixed' }
  })
}

export function del(ids) {
  return request({
    url: '/brands',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }

