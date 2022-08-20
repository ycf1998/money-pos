import defaultSettings from '@/settings'

export default function getPageTitle(pageTitle) {
  // 【qk-money】：租户标签title
  const title = window.tenant ? window.tenant.tenantName : defaultSettings.title
  if (pageTitle) {
    return `${title} - ${pageTitle}`
  }
  return `${title}`
}
