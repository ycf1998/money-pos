import instance from '@/api/axios.js'
import mock from './mock.js'

export function req(options) {
    const onlyUI = import.meta.env.VITE_ONLY_UI
    if (onlyUI) {
        onlyUI === 'log' ? console.log(JSON.stringify(options)) :alert(JSON.stringify(options))
        const defaultKey = `${options['method']}_default`
        const key = `${options['method']}_${options['url']}`
        return new Promise((resolve) => resolve(mock[key] || mock[defaultKey]))
    } else {
        return instance.request(options)
    }
}

export function reqForm(options) {
    options.headers = {}
    options.headers['Content-Type'] = 'application/x-www-form-urlencoded'
    return req(options)
}

export function reqFormData(options) {
    options.headers = {}
    options.headers['Content-Type'] = 'multipart/form-data'
    return req(options)
}

export function reqMixed(options, {key, jsonData}, formData) {
    const data = new FormData()
    if (key && jsonData) {
        data.append(key, new Blob([JSON.stringify(jsonData)], {
            type: 'application/json'
        }))
    }
    if (formData) {
        Object.entries(formData).forEach(kv => data.append(kv[0], kv[1]))
    }
    options.data = data
    options.headers = {}
    options.headers['Content-Type'] = 'multipart/mixed'
    return req(options)
}

export default req