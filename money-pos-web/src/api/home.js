import request from './request'

export function getHomeCount() {
  return request({
    url: '/home/count',
    method: 'get'
  })
}

export default { getHomeCount }
