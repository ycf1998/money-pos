import {useCookies} from '@vueuse/integrations/useCookies'

const tokenKey = 'accessToken'
export const setToken = (token) => useCookies().set(tokenKey, token)
export const getToken = () => useCookies().get(tokenKey)
export const removeToken = () => useCookies().remove(tokenKey)
