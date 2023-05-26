import request from '@/api/request'

export function buildRouter() {
  return request({
    url: '/auth/router',
    method: 'get'
  })
}

/**
 * 登录
 * @param {*} data
 * @returns
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 获取个人信息
 * @returns
 */
export function getInfo() {
  return request({
    url: '/auth/own',
    method: 'get'
  })
}

/**
 * 登出
 * @returns
 */
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}
