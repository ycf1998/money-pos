import MoneyConfig from "@/money.config.js";

import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon.vue'

import 'vue3-perfect-scrollbar/dist/vue3-perfect-scrollbar.css'
import PerfectScrollbar from 'vue3-perfect-scrollbar'

import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/style/reset-element.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox, ElNotification, ElLoadingService} from "element-plus";

export default {
    install: (app, options) => {
        app.config.globalProperties.$money = MoneyConfig
        app.config.globalProperties.$message = ElMessage
        app.config.globalProperties.$notify = ElNotification
        app.config.globalProperties.$msgbox = ElMessageBox
        app.config.globalProperties.$alert = ElMessageBox.alert
        app.config.globalProperties.$confirm = ElMessageBox.confirm
        app.config.globalProperties.$prompt = ElMessageBox.prompt
        app.config.globalProperties.$loading = ElLoadingService

        for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
            app.component(key, component)
        }
        app.component('svg-icon', SvgIcon)

        app.use(PerfectScrollbar)
    }
}