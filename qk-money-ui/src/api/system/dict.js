import req from '../index.js'

export default {
    list: (query) => req({
        url: '/dict',
        method: 'GET',
        params: query,
    }),
    add: (data) => req({
        url: '/dict',
        method: 'POST',
        data,
    }),
    edit: (data) => req({
        url: '/dict',
        method: 'PUT',
        data,
    }),
    del: (ids) => req({
        url: '/dict',
        method: 'DELETE',
        data: ids,
    }),
    getDetail: (dict) => req({
        url: `/dict/detail?dict=${dict}`,
        method: 'GET'
    }),
    addDetail: (data) => req({
        url: '/dict/detail',
        method: 'POST',
        data,
    }),
    editDetail: (data) => req({
        url: '/dict/detail',
        method: 'PUT',
        data,
    }),
    delDetail: (ids) => req({
        url: '/dict/detail',
        method: 'DELETE',
        data: ids,
    }),

    loadDict: async (dictList = []) => {
        const dictMap = {}
        for (const dict of dictList) {
            const {data} = await req({
                url: `/dict/detail?dict=${dict}`,
                method: 'GET'
            })
            if (data && data.length > 0) {
                dictMap[dict] = data.map(dd => {
                    // TODO 多语言 先默认中文
                    dd.desc = dd.cnDesc
                    return {
                        desc: dd.desc,
                        value: dd.value,
                    }
                })
                dictMap[dict + 'Kv'] = {}
                data.map(dd => dictMap[dict + 'Kv'][dd.value] = dd.desc)
            }
        }
        return dictMap
    }
}