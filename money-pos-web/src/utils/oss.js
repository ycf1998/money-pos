import MoneyConfig from '@/money.config'
import TokenManage from '@/utils/tokenManage'

export const TYPE = {
  LOCAL: 1,
  CLOUD: 2
}

/**
 * 获取请求头部，用于直接上传携带
 * @returns
 */
export function getHeaders () {
  const headers = {}
  headers[MoneyConfig.tokenHeader] = TokenManage.getToken()
  return headers
}

/**
 * 加载图片
 *
 * @param {*} url
 * @param {*} type
 * @returns
 */
export function loadImage (url, type = TYPE.LOCAL) {
  if (url && !url.includes('http')) {
    if (type === TYPE.LOCAL) {
      url = process.env.VUE_APP_BASE_API + MoneyConfig.localOSSPath + url
    } else if (type === TYPE.CLOUD) {
      url = MoneyConfig.cloudOSSPath + url
    }
  }
  return url
}

export default { TYPE, loadImage, getHeaders }
