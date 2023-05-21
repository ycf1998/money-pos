import MoneyConfig from '@/money.config'
import axios from 'axios'
import { Message } from 'element-ui'
import TokenManage from '@/utils/tokenManage'
import store from '@/store'

const instance = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 300000,
  retry: 3,
})

// 请求拦截器
instance.interceptors.request.use(
  async config => {
    // 租户处理
    let tenantQuery = window.location.search.substring(1).match(new RegExp('(^|&)tenant=([^&]*)(&|$)', 'i'))
    if (tenantQuery) {
      if (!window.tenant) {
        const tenantCode = tenantQuery[2].replace('/', '')
        window.tenant = await axios
          .get(`${process.env.VUE_APP_BASE_API}/tenants/byCode?code=${tenantCode}`)
          .then(res => res.data?.data)
      }
      // 租户
      config.headers[MoneyConfig.tenantHeader] = window.tenant.id
    }
    // 鉴权头
    config.headers[MoneyConfig.tokenHeader] = `${MoneyConfig.tokenType} ${TokenManage.getToken()}`
    // requestId
    config.headers[MoneyConfig.requestIdHeader] = new Date().getTime()
    // 国际化
    config.headers[MoneyConfig.i18nHeader] = MoneyConfig.lang
    // 时区
    config.headers[MoneyConfig.timezoneHeader] = MoneyConfig.timezone
    return config
  },
  error => {
    console.error(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
instance.interceptors.response.use(
  response => {
    const body = response.data
    if (!body.code) return body

    if (body.code !== 200) {
      const errorMsg = body.message || 'Error'
      Message({
        message: errorMsg,
        type: 'error',
        duration: 5 * 1000,
      })
      return Promise.reject(new Error(errorMsg))
    }
    return body
  },
  error => {
    const status = error.response.status
    if (status === 401) {
      Message({
        message: '登录状态已过期',
        type: 'error',
        duration: 5 * 1000,
      })
      // 跳转登录页
      store.dispatch('user/resetToken').then(() => {
        location.reload()
      })
    } else if (status === 403) {
      Message({
        message: '您没有权限哦',
        type: 'error',
        duration: 5 * 1000,
      })
    } else {
      Message({
        message: error.message,
        type: 'error',
        duration: 5 * 1000,
      })
    }
    return Promise.reject(error)
  }
)

export default instance
