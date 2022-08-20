import request from '@/api/request'

export function listGoods(barcode) {
  return request({
    url: '/pos/goods',
    method: 'get',
    params: {
      barcode
    }
  })
}

export function listMember(member) {
  return request({
    url: '/pos/members',
    method: 'get',
    params: {
      member
    }
  })
}

export function settleAccounts(data) {
  return request({
    url: '/pos/settleAccounts',
    method: 'post',
    data
  })
}

export default { listGoods, listMember, settleAccounts }
