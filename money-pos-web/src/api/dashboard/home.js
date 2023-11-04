import req from '../index.js'

export default {
    getHomeCount: () => req({
        url: '/home/count',
        method: 'GET',
    }),
}