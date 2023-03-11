import request from '@/api/request'

export function loadProvinces() {
  return request({
    url: '/provinces',
    method: 'get'
  })
}

export function loadCities(province) {
  return request({
    url: `/cities?province=${province}`,
    method: 'get'
  })
}

export function loadDistricts(city) {
  return request({
    url: `/districts?city=${city}`,
    method: 'get'
  })
}

export default { loadProvinces, loadCities, loadDistricts }
