const config = {
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

  // 语言
  lang: 'zh-cn',
  // 时区
  timezone: 'GMT+08:00'
}

export default config
