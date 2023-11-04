import req from './index.js'

export default {
    list: (query) => req({
        url: '/template',
        method: 'GET',
        params: query,
    }),
    add: (data) => req({
        url: '/template',
        method: 'POST',
        data,
    }),
    edit: (data) => req({
        url: '/template',
        method: 'PUT',
        data,
    }),
    del: (ids) => req({
        url: '/template',
        method: 'DELETE',
        data: ids,
    })
}