import request from '@/api/request'

export function count(params) {
  return request({
    url: `/oms/order/count`,
    method: 'get',
    params
  })
}

export function getDetial(id) {
  return request({
    url: `/oms/order/detail?id=${id}`,
    method: 'get'
  })
}

export function returnOrder(data) {
  return request({
    url: `/oms/order/returnOrder`,
    method: 'delete',
    data
  })
}

export function returnGoods(data) {
  return request({
    url: `/oms/order/returnGoods`,
    method: 'delete',
    data
  })
}

export function getLog(id) {
  return request({
    url: `/oms/orderLog?id=${id}`,
    method: 'get'
  })
}

export default { count, getDetial, returnOrder, returnGoods, getLog }

