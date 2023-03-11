import request from '@/api/request'
import qs from 'qs'

/**
 * 通用主列表查询
 * @param {*} url
 * @param {*} params
 * @returns
 */
export function list(url, params) {
  return request({
    url: url + '?' + qs.stringify(params, { indices: false }),
    method: 'get'
  })
}

export default { list }
