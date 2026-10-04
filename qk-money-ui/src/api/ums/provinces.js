import req from '../index.js'

export default {
  loadProvinces: () => req({
    url: '/provinces',
    method: 'GET'
  }),
  loadCities: (province) => req({
    url: `/cities?province=${province}`,
    method: 'GET'
  }),
  loadDistricts: (city) => req({
    url: `/districts?city=${city}`,
    method: 'GET'
  }),
}

