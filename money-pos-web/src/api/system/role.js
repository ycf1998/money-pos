import req from '../index.js'

export default {
    list: (query) => req({
        url: '/roles',
        method: 'GET',
        params: query,
    }),
    add: (data) => req({
        url: '/roles',
        method: 'POST',
        data,
    }),
    edit: (data) => req({
        url: '/roles',
        method: 'PUT',
        data,
    }),
    del: (ids) => req({
        url: '/roles',
        method: 'DELETE',
        data: ids,
    }),
    getAll: () => req({
        url: '/roles/all',
        method: 'GET'
    }),
    configurePermissions: (id, permissionIds) => req({
        url: `/roles/${id}/permission`,
        method: 'POST',
        data: permissionIds
    })
}