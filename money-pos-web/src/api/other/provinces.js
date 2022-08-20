import request from '@/api/request'

export function loadProvinces() {
  return request({
    url: '/address/provinces',
    method: 'get'
  })
}

export function loadCities(province) {
  return request({
    url: `/address/cities?province=${province}`,
    method: 'get'
  })
}

export function loadDistricts(city) {
  return request({
    url: `/address/districts?city=${city}`,
    method: 'get'
  })
}

export default { loadProvinces, loadCities, loadDistricts }
