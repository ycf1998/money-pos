import req from '../index.js'

export default {
  list: (query) => req({
    url: '/ums/member',
    method: 'GET',
    params: query,
  }),
  add: (data) => req({
    url: '/ums/member',
    method: 'POST',
    data,
  }),
  edit: (data) => req({
    url: '/ums/member',
    method: 'PUT',
    data,
  }),
  del: (ids) => req({
    url: '/ums/member',
    method: 'DELETE',
    data: ids,
  })
}

