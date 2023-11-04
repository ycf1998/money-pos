import {req, reqMixed} from '../index.js'

export default {
    getSelect: () => req({
        url: '/gms/goodsCategory/select',
        method: 'GET',
    }),
    getTree: () => req({
        url: '/gms/goodsCategory/tree',
        method: 'GET',
    }),
    add: (data) => reqMixed({
        url: '/gms/goodsCategory',
        method: 'POST',
    }, {
        key: 'goodsCategory',
        jsonData: data
    }, {
        icon: data.logoFile
    }),
    edit: (data) => reqMixed({
        url: '/gms/goodsCategory',
        method: 'PUT',
    }, {
        key: 'goodsCategory',
        jsonData: data
    }, {
        icon: data.logoFile
    }),
    del: (ids) => req({
        url: '/gms/goodsCategory',
        method: 'DELETE',
        data: ids,
    })
}