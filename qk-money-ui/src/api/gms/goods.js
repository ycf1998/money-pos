import {req, reqMixed} from '../index.js'

export default {
    list: (query) => req({
        url: '/gms/goods',
        method: 'GET',
        params: query
    }),
    add: (data) => reqMixed({
        url: '/gms/goods',
        method: 'POST',
    }, {
        key: 'goods',
        jsonData: data
    }, {
        pic: data.picFile
    }),
    edit: (data) => reqMixed({
        url: '/gms/goods',
        method: 'PUT',
    }, {
        key: 'goods',
        jsonData: data
    }, {
        pic: data.picFile
    }),
    del: (ids) => req({
        url: '/gms/goods',
        method: 'DELETE',
        data: ids,
    })
}