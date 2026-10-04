import req from '../index.js'

export default {
    list: (query) => req({
        url: '/users',
        method: 'GET',
        params: query,
    }),
    add: (data) => req({
        url: '/users',
        method: 'POST',
        data,
    }),
    edit: (data) => req({
        url: '/users',
        method: 'PUT',
        data,
    }),
    del: (ids) => req({
        url: '/users',
        method: 'DELETE',
        data: ids,
    }),
    updateInfo: (data) => req({
        url: '/users/updateProfile',
        method: 'POST',
        data,
    }),
    changePassword: (data) => req({
        url: '/users/changePassword',
        method: 'POST',
        data,
    }),
}