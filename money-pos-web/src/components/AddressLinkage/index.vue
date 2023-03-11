<template>
  <el-row class="address" type="flex" justify="space-between" :style="{flexDirection: direct}">
    <el-col>
      <el-select v-model="p" placeholder="省份" style="width: 220px;" @change="changeProvince">
        <el-option v-for="item in provinces" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-col>
    <el-col>
      <el-select v-model="c" placeholder="城市" style="width: 220px;" @change="changeCity">
        <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-col>
    <el-col>
      <el-select v-model="d" placeholder="地区" style="width: 220px;" @change="changeDistrict">
        <el-option v-for="item in districts" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-col>
  </el-row>
</template>

<script>
import provincesApi from '@/api/ums/provinces'

export default {
  name: 'AddressLinkage',
  props: {
    direct: {
      type: String,
      default: 'column'
    },
    province: {
      type: String,
      default: null
    },
    city: {
      type: String,
      default: null
    },
    district: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      p: this.province,
      c: this.city,
      d: this.district,
      provinces: [],
      cities: [],
      districts: []
    }
  },
  watch: {
    district: function (val) {
      this.p = this.province
      this.c = this.city
      this.d = this.district
      if (val) {
        this.changeProvince(this.province)
        this.changeCity(this.city)
        this.changeDistrict(this.district)
      }
    }
  },
  created() {
    provincesApi.loadProvinces().then(res => {
      this.provinces = res.data
    })
  },
  methods: {
    changeProvince(val) {
      this.p = val
      this.c = null
      this.d = null
      provincesApi.loadCities(val).then(res => {
        this.cities = res.data
      })
      this.selectChange()
    },
    changeCity(val) {
      this.c = val
      this.d = null
      provincesApi.loadDistricts(val).then(res => {
        this.districts = res.data
      })
      this.selectChange()
    },
    changeDistrict(val) {
      this.d = val
      this.selectChange()
    },
    selectChange() {
      this.$emit('select-change', this.p, this.c, this.d)
    }
  }
}
</script>

<style lang="scss" scoped>
.address .el-col {
  padding: 5px 0;
}
</style>
