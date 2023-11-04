import req from '../index.js'

export default {
    getDyRoutes: () => req({
        url: '/auth/router',
        method: 'GET'
    }),
    login: (data) => req({
        url: '/auth/login',
        method: 'POST',
        data
    }),
    getInfo: () => req({
        url: '/auth/own',
        method: 'GET'
    }),
    logout: () => req({
        url: '/auth/logout',
        method: 'POST',
    }),
}