import {req, reqMixed} from '../index.js'

export default {
    list: (query) => req({
        url: '/tenants',
        method: 'GET',
    }),
    add: (data) => reqMixed({
        url: '/tenants',
        method: 'POST',
    }, {
        key: 'tenant',
        jsonData: data
    }, {
        logo: data.logoFile
    }),
    edit: (data) => reqMixed({
        url: '/tenants',
        method: 'PUT',
    }, {
        key: 'tenant',
        jsonData: data
    }, {
        logo: data.logoFile
    }),
    del: (ids) => req({
        url: '/tenants',
        method: 'DELETE',
        data: ids,
    })
}