/**
 * 全局参数
 * 在 plugins/index.js 配置 app.config.globalProperties
 */
import {getCurrentInstance} from 'vue';

export function useGlobalProp() {
    const {appContext: {config: {globalProperties}}} = getCurrentInstance()
    return globalProperties
}

