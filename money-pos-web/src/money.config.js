export default {
    // 语言
    lang: 'zh-cn',
    // 时区
    timezone: 'GMT+08:00',

    // 租户请求头
    tenantHeader: 'Y-tenant',
    // token类型
    tokenType: 'Bearer',
    // token请求头
    tokenHeader: 'Authorization',
    // requestId请求头
    requestIdHeader: 'X-qk-request',
    // 多语言请求头
    i18nHeader: 'X-qk-lang',
    // 时区请求头
    timezoneHeader: 'X-qk-timezone',
    // 静态OSS路径
    localOSSPath: '/assets/',
    // 云OSS路径
    cloudOSSPath: '',

    /**
     * 获取完整路径
     * @param path
     * @param cloud
     * @returns {string}
     */
    getOssUrl(path, cloud = false) {
        if (path && !path.includes('http')) {
            if (cloud) {
               path = config.cloudOSSPath + path
            } else {
                path = import.meta.env.VITE_BASE_URL + this.localOSSPath + path
            }
        }
        return path
    }
}