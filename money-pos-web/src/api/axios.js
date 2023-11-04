import axios from 'axios'
import {useUserStore} from "@/store";
import {ElMessage} from "element-plus";
import {getToken} from "@/composables/token.js";
import MoneyConfig from "@/money.config.js";

const instance = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000,
})

instance.interceptors.request.use(async function (config) {
    // 鉴权头
    const token = getToken()
    if (token) config.headers[MoneyConfig.tokenHeader] = `${MoneyConfig.tokenType} ${getToken()}`
    // 租户
    const tenantCode = window.location.search.match(/(^|&|\?)tenant=([^&]*)(&|$)/i)?.[2]
    if (tenantCode && (window.tenant == null || window.tenant.tenantCode !== tenantCode)) {
        const body = await axios
            .get(`${config.baseURL}/tenants/byCode?code=${tenantCode}`)
            .then(res => res.data)
        if (body.data) {
            window.tenant = body.data
        } else {
            throw new Error('租户不存在')
        }
    }
    if (window.tenant) config.headers[MoneyConfig.tenantHeader] = window.tenant.id
    // requestId
    config.headers[MoneyConfig.requestIdHeader] = new Date().getTime()
    // 国际化
    config.headers[MoneyConfig.i18nHeader] = MoneyConfig.lang
    // 时区
    config.headers[MoneyConfig.timezoneHeader] = MoneyConfig.timezone
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
})

instance.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    const body = response.data
    if (!body.code) return body

    if (body.code !== 200) {
        const errorMsg = body.message || 'Server Error'
        ElMessage.error(errorMsg)
        return Promise.reject(new Error(errorMsg))
    }
    return body;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    const status = parseInt(error.response && error.response.status)
    if (status === 401) {
        ElMessage.error('登录状态已过期')
        useUserStore().logout()
    } else if (status === 403) {
        ElMessage.error('您没有权限哦')
    } else {
        ElMessage.error(error.message)
    }
    return Promise.reject(error);
})

export default instance