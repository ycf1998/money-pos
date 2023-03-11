<template>
  <div class="dashboard-container">
    <el-tabs v-model="activeTab" @tab-click="handleClick">
      <el-tab-pane v-for="tab in totalTab" :key="tab.name" :label="tab.label" :name="tab.name">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="6" style="padding-top: 10px;">
            <el-card shadow class="box-card">
              <div slot="header" class="clearfix">
                <span>{{ tab.label }}订单数</span>
              </div>
              <el-row type="flex" justify="space-between" align="center">
                <img src="orderCount.png" width="45" height="35">
                <count-to :start-val="0" :decimals="2" :end-val="homeCount[tab.name].orderCount" :duration="3200" class="card-panel-num" />
              </el-row>
            </el-card>
          </el-col>
          <el-col :xs="12" :sm="6" style="padding-top: 10px;">
            <el-card shadow class="box-card">
              <div slot="header" class="clearfix">
                <span>{{ tab.label }}销售额</span>
              </div>
              <el-row type="flex" justify="space-between" align="center">
                <img src="sale.png" width="45" height="35">
                <count-to :start-val="0" :decimals="2" :end-val="homeCount[tab.name].saleCount" :duration="3200" class="card-panel-num" />
              </el-row>
            </el-card>
          </el-col>
          <el-col :xs="12" :sm="6" style="padding-top: 10px;">
            <el-card shadow class="box-card">
              <div slot="header" class="clearfix">
                <span>{{ tab.label }}利润</span>
              </div>
              <el-row type="flex" justify="space-between" align="center">
                <img src="profit.png" width="45" height="35">
                <count-to :start-val="0" :decimals="2" :end-val="homeCount[tab.name].profit" :duration="3200" class="card-panel-num" />
              </el-row>
            </el-card>
          </el-col>
          <el-col :xs="12" :sm="6" style="padding-top: 10px;">
            <el-card shadow class="box-card">
              <div slot="header" class="clearfix">
                <span>库存价值</span>
              </div>
              <el-row type="flex" justify="space-between" align="center">
                <img src="stockValue.png" width="45" height="35">
                <count-to :start-val="0" :decimals="2" :end-val="homeCount.inventoryValue" :duration="3200" class="card-panel-num" />
              </el-row>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import homeApi from '@/api/home'

export default {
  name: 'Dashboard',
  components: {
    CountTo
  },
  data() {
    return {
      activeTab: 'today',
      totalTab: [
        {
          label: '今日',
          name: 'today'
        },
        {
          label: '本月',
          name: 'month'
        },
        {
          label: '本年',
          name: 'year'
        },
        {
          label: '总',
          name: 'total'
        }
      ],
      homeCount: {
        today: {},
        month: {},
        year: {},
        total: {}
      }
    }
  },
  created() {
    homeApi.getHomeCount().then(res => { this.homeCount = res.data })
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
span.card-panel-num {
  font-size: 25px;
}

@media (max-width: 650px) {
  .el-tab-pane img {
    display: none;
  }
}
</style>
