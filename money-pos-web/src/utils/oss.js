import moneyConfig from '@/money.config'
import { getToken } from '@/utils/auth'

export const TYPE = {
  LOCAL: 1,
  CLOUD: 2
}

/**
 * 获取请求头部，用于直接上传携带
 * @returns
 */
export function getHeaders() {
  const headers = {}
  headers[moneyConfig.tokenHeader] = getToken()
  return headers
}

/**
 * 加载图片
 *
 * @param {*} url
 * @param {*} type
 * @returns
 */
export function loadImage(url, type = TYPE.LOCAL) {
  if (url && !url.includes('http')) {
    if (type === TYPE.LOCAL) {
      url = process.env.VUE_APP_BASE_API + moneyConfig.localOSSPath + url
    } else if (type === TYPE.CLOUD) {
      url = moneyConfig.cloudOSSPath + url
    }
  }
  return url
}

export default { TYPE, loadImage, getHeaders }
