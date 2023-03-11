import moneyConfig from '@/money.config'
import tokenManage from '@/utils/tokenManage'
import axios from 'axios'
import store from '@/store'
import { Message } from 'element-ui'

const service = axios.create({
  // url = base url + request url
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 300000,
  retry: 3
})

// 请求拦截器
service.interceptors.request.use(
  async config => {
    // 【qk-money】：租户
    let tenantCode = window.location.search.substring(1).match(new RegExp('(^|&)tenant=([^&]*)(&|$)', 'i'))
    if (tenantCode) {
      if (store.getters.tenant == null) {
        tenantCode = tenantCode[2].replace('/', '')
        const tenant = await axios.get(`${process.env.VUE_APP_BASE_API}/tenants/byCode?code=${tenantCode}`).then(res => res.data?.data)
        if (tenant) {
          store.commit('user/SET_TENANT', tenant.id)
          // eslint-disable-next-line require-atomic-updates
          window.tenant = tenant
        } else {
          // 取消请求
          const CancelToken = axios.CancelToken
          const source = CancelToken.source()
          source(Message({
            message: '租户信息错误',
            type: 'error',
            duration: 5 * 1000
          }))
        }
      }
      config.headers[moneyConfig.tenantHeader] = store.getters.tenant
    }
    // 【qk-money】：access token
    if (store.getters.token) {
      config.headers[moneyConfig.tokenHeader] = `${moneyConfig.tokenType} ${tokenManage.getToken()}`
    }
    // 【qk-money】：request上下文请求头
    config.headers[moneyConfig.requestIdHeader] = new Date().getTime()
    config.headers[moneyConfig.i18nHeader] = moneyConfig.lang
    config.headers[moneyConfig.timezoneHeader] = moneyConfig.timezone
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    // 针对后端非R返回（如文件下载直接的数据流）
    if (!res.code) {
      return res
    }
    if (res.code !== 200) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    if (error.response.status === 401) {
      Message({
        message: '登录状态已过期',
        type: 'error',
        duration: 5 * 1000
      })
      store.dispatch('user/resetToken').then(() => {
        location.reload()
      })
    } else if (error.response.status === 403) {
      Message({
        message: '您没有权限哦',
        type: 'error',
        duration: 5 * 1000
      })
    } else {
      Message({
        message: error.message,
        type: 'error',
        duration: 5 * 1000
      })
    }
    return Promise.reject(error)
  }
)

export default service
