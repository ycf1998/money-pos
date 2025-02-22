import { getToken } from '@/composables/token.js';
import MoneyConfig from '@/money.config.js';
import { useUserStore } from '@/store';
import axios from 'axios';
import { ElMessage } from 'element-plus';

// 创建 axios 实例
const instance = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000,
});

// 请求拦截器
instance.interceptors.request.use(
    async (config) => {
        // 添加鉴权头
        const token = getToken();
        if (token) {
            config.headers[MoneyConfig.tokenHeader] = `${MoneyConfig.tokenType} ${token}`;
        }

        // 处理租户信息
        const tenantCode = window.location.search.match(/(^|&|\?)tenant=([^&]*)(&|$)/i)?.[2];
        if (tenantCode && (window.tenant == null || window.tenant.tenantCode !== tenantCode)) {
            try {
                const { data } = await axios.get(`${config.baseURL}/tenants/byCode?code=${tenantCode}`);
                if (data.data) {
                    window.tenant = data.data;
                } else {
                    throw new Error('租户不存在');
                }
            } catch (error) {
                console.error('获取租户信息失败', error);
                throw error;
            }
        }
        if (window.tenant) {
            config.headers[MoneyConfig.tenantHeader] = window.tenant.id;
        }

        // 添加请求 ID
        config.headers[MoneyConfig.requestIdHeader] = new Date().getTime();

        // 添加国际化头
        config.headers[MoneyConfig.i18nHeader] = MoneyConfig.lang;

        // 添加时区头
        config.headers[MoneyConfig.timezoneHeader] = MoneyConfig.timezone;

        return config;
    },
    (error) => {
        // 请求错误处理
        console.error('请求发送失败', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
instance.interceptors.response.use(
    (response) => {
        const body = response.data;

        // 如果响应体没有 code 字段，直接返回
        if (!body.code) return body;

        // 处理非 200 状态码
        if (body.code !== 200) {
            const errorMsg = body.message || '服务器错误';
            ElMessage.error(errorMsg);
            return Promise.reject(new Error(errorMsg));
        }

        return body;
    },
    (error) => {
        // 网络错误处理
        if (error.message === 'Network Error') {
            ElMessage.error('网络错误，请检查网络连接');
            return Promise.reject(error);
        }

        // HTTP 状态码处理
        const status = error.response?.status;
        switch (status) {
            case 401:
                ElMessage.error('登录状态已过期');
                useUserStore().logout();
                break;
            case 403:
                ElMessage.error('您没有权限哦');
                break;
            default:
                ElMessage.error(error.message || '请求失败');
        }

        return Promise.reject(error);
    }
);

export default instance;