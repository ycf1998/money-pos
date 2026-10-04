import req from '../index.js'

export default {
    list: (query) => req({
        url: '/permissions',
        method: 'GET',
        params: query,
    }),
    add: (data) => req({
        url: '/permissions',
        method: 'POST',
        data,
    }),
    edit: (data) => req({
        url: '/permissions',
        method: 'PUT',
        data,
    }),
    del: (ids) => req({
        url: '/permissions',
        method: 'DELETE',
        data: ids,
    })
}