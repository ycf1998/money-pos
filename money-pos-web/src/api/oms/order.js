import request from '@/api/request'

export function count(params) {
  return request({
    url: `/orders/count`,
    method: 'get',
    params
  })
}

export function getDetial(id) {
  return request({
    url: `/orders/detail?id=${id}`,
    method: 'get'
  })
}

export function returnOrder(data) {
  return request({
    url: `/orders/returnOrder`,
    method: 'delete',
    data
  })
}

export function returnGoods(data) {
  return request({
    url: `/orders/returnGoods`,
    method: 'delete',
    data
  })
}

export function getLog(id) {
  return request({
    url: `/orderLog?id=${id}`,
    method: 'get'
  })
}

export default { count, getDetial, returnOrder, returnGoods, getLog }

