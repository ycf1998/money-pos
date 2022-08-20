import request from '@/api/request'

export function getTenantId(code) {
  return request({
    url: `/tenant/byCode?code=${code}`,
    method: 'get'
  })
}

export function add(data) {
  const formData = new FormData()
  formData.append('tenant', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }))
  formData.append('logo', data.logoFile)
  return request({
    url: '/tenants',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/mixed' }
  })
}

export function edit(data) {
  const formData = new FormData()
  formData.append('tenant', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }))
  formData.append('logo', data.logoFile)
  return request({
    url: '/tenants',
    method: 'put',
    data: formData,
    headers: { 'Content-Type': 'multipart/mixed' }
  })
}

export function del(ids) {
  return request({
    url: '/tenants',
    method: 'delete',
    data: ids
  })
}

export default { add, edit, del }

