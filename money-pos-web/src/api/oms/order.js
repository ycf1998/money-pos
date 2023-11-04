import req from '../index.js'

export default {
    list: (query) => req({
        url: `/oms/order`,
        method: 'GET',
        params: query
    }),
    getCount: (query) => req({
        url: `/oms/order/count`,
        method: 'GET',
        params: query
    }),
    getDetail: (id) => req({
        url: `/oms/order/detail?id=${id}`,
        method: 'GET'
    }),
    returnOrder: (data) => req({
        url: `/oms/order/returnOrder`,
        method: 'DELETE',
        data
    }),
    returnGoods: (data) => req({
        url: `/oms/order/returnGoods`,
        method: 'DELETE',
        data
    })
}