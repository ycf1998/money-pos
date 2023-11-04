import NProgress from "nprogress";
import 'nprogress/nprogress.css'
import {getToken, removeToken, setToken} from "@/composables/token.js";
import {useAppStore, useUserStore} from "@/store/index.js";

// 白名单
const whitelist = ['/login']

export default function (router) {
    router.beforeEach(async (to) => {
        if (import.meta.env.VITE_ONLY_UI) {
            setToken('only_ui')
        }
        const hasToken = getToken()
        if (hasToken && !useAppStore().menus) {
            await useAppStore().loadRoutes()
            return to.fullPath
        }
    })

    router.beforeEach(async (to, from, next) => {
        NProgress.start()
        const hasToken = getToken()
        if (hasToken && to.path === '/login') {
            next({path: '/'})
        } else if (hasToken) {
            try {
                await useUserStore().loadInfo()
                next()
            } catch (e) {
                removeToken()
                next(`/login?redirect=${to.path}`)
            }
        } else if (whitelist.indexOf(to.path) !== -1) {
            next()
        } else {
            next(`/login?redirect=${to.path}`)
        }
    })

    router.afterEach(() => {
        NProgress.done()
    })

    return router
}
