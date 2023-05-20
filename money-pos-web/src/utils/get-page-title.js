import MoneyConfig from '@/money.config'

export default function getPageTitle (pageTitle) {
  const title = window.tenant ? window.tenant.tenantName : MoneyConfig.title
  if (pageTitle) {
    return `${title} - ${pageTitle}`
  }
  return `${title}`
}
