import request from '@/api/request'

export function selectCategory() {
  return request({
    url: '/goodsCategories/select',
    method: 'get'
  })
}

export function getTree() {
  return request({
    url: '/goodsCategories/tree',
    method: 'get'
  })
}

export function add(data) {
  const formData = new FormData()
  formData.append('goodsCategory', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }))
  formData.append('icon', data.iconFile)
  return request({
    url: '/goodsCategories',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/mixed' }
  })
}

export function edit(data) {
  const formData = new FormData()
  formData.append('goodsCategory', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }))
  formData.append('icon', data.iconFile)
  return request({
    url: '/goodsCategories',
    method: 'put',
    data: formData,
    headers: { 'Content-Type': 'multipart/mixed' }
  })
}

export function del(ids) {
  return request({
    url: '/goodsCategories',
    method: 'delete',
    data: ids
  })
}

export default { getTree, add, edit, del }

