import {defineStore} from 'pinia'
import authApi from '@/api/system/auth.js'
import userApi from '@/api/system/user.js'
import {getToken, removeToken, setToken} from "@/composables/token.js"

export const useUserStore = defineStore('user', {
    state: () => ({
        info: null,
        roles: null,
        permissions: null
    }),
    getters: {
        level: (state) => state.roles[0].level,
        permissionCode: (state) =>
            [...state.roles.map(e => e.roleCode), ...state.permissions.map(e => e.permission)].filter(e => e.length > 0)
    },
    actions: {
        /**
         * 是否有权限
         * @param codes
         * @returns {*|boolean}
         */
        hasPermission(codes) {
            if (this.level === 0) return true
            if (!codes) return false
            if (Array.isArray(codes)) {
                return this.permissionCode.some(e => codes.includes(e))
            }
            return this.permissionCode.includes(codes)
        },
        /**
         * 登录
         * @param data
         * @returns {Promise<unknown>}
         */
        async login(data) {
            const res = await authApi.login(data)
            setToken(res.data.accessToken)
        },
        /**
         * 加载用户信息
         * @returns {Promise<unknown>}
         */
        async loadInfo() {
            const res = await authApi.getInfo()
            const {data} = res
            this.info = data.info
            this.roles = data.roles
            this.permissions = data.permissions
            return data
        },
        /**
         * 登出
         * @returns {Promise<void>}
         */
        async logout() {
            if (getToken()) await authApi.logout().then(() => removeToken())
            this.info = null
            this.roles = null
            this.permissions = null
            removeToken()
            window.location.reload()
        },
        /**
         * 更新信息
         * @param data
         * @returns {Promise<unknown>}
         */
        async updateInfo(data) {
            await userApi.updateInfo(data)
        },
        /**
         * 修改密码
         * @param data
         * @returns {Promise<unknown>}
         */
        async changePassword(data) {
            await userApi.changePassword(data)
            this.logout()
        }
    }
})