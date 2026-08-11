import req from '../index.js'

export default {
    list: (params) => req({method: 'GET', url: '/template', params}),
    add: (data) => req({method: 'POST', url: '/template', data}),
    edit: (data) => req({method: 'PUT', url: '/template', data}),
    del: (ids) => req({method: 'DELETE', url: '/template', data: ids}),
}