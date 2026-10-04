import {defineStore} from 'pinia'
import authApi from '@/api/system/auth.js'
import Layout from "@/layouts/DashboardLayout.vue";

const views = import.meta.glob('@/views/**')

export const useAppStore = defineStore('app', {
    state: () => ({
        menus: null,
    }),
    actions: {
        async loadRoutes() {
            const {data} = await authApi.getDyRoutes()
            this.menus = this._loadMenus(data, true)
            console.log(this.menus)
        },
        _loadMenus(menus, first) {
            if (!menus) return []
            return menus.map(e => {
                const menu = Object.fromEntries(
                Object.entries({
                    name: e.name,
                    path: (first ? '/' : '') + e.path,
                    component: first ? Layout : views[`/src/views/${e.component}.vue`],
                    meta: e.meta,
                    iframe: e.iframe,
                    hidden: e.hidden,
                }).filter(([_, v]) => v)
            )
                if (e.children) {
                    menu.children = this._loadMenus(e.children, false)
                    menu.redirect = {name: e.children[0].name}
                }
                if (first) {
                    this.$router.addRoute(menu)
                }
                return menu
            })
        }
    }
})