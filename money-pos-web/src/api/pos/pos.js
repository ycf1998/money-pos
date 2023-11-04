import req from '../index.js'

export default {
    listGoods: (barcode) => req({
        url: '/pos/goods',
        method: 'GET',
        params: {
            barcode
        }
    }),
    listMember: (member) => req({
        url: '/pos/members',
        method: 'GET',
        params: {
            member
        }
    }),
    settleAccounts: (data) => req({
        url: '/pos/settleAccounts',
        method: 'POST',
        data
    })
}