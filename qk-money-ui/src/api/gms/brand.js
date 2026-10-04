import {req, reqMixed} from '../index.js'

export default {
    getSelect: () => req({
        url: '/gms/brand/select',
        method: 'GET'
    }),
    list: (query) => req({
        url: '/gms/brand',
        method: 'GET',
        params: query
    }),
    add: (data) => reqMixed({
        url: '/gms/brand',
        method: 'POST',
    }, {
        key: 'brand',
        jsonData: data
    }, {
        logo: data.logoFile
    }),
    edit: (data) => reqMixed({
        url: '/gms/brand',
        method: 'PUT',
    }, {
        key: 'brand',
        jsonData: data
    }, {
        logo: data.logoFile
    }),
    del: (ids) => req({
        url: '/gms/brand',
        method: 'DELETE',
        data: ids,
    })
}