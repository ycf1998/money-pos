<template>
  <el-row>
    <el-col :span="18">
      <el-tabs v-model="activeTab" tab-position="left">
        <el-tab-pane label="今日" name="today">
          <el-row :gutter="40" class="panel-group">
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="list" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    今日订单数
                  </div>
                  <count-to :start-val="0" :end-val="homeCount.today.orderCount" :duration="2600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('shoppings')">
                <div class="card-panel-icon-wrapper icon-shopping">
                  <svg-icon icon-class="shopping" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    今日销售额
                  </div>
                  <count-to :start-val="0" :decimals="2" :end-val="homeCount.today.saleCount" :duration="3600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('purchases')">
                <div class="card-panel-icon-wrapper icon-money">
                  <svg-icon icon-class="money" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    今日利润
                  </div>
                  <count-to :start-val="0" :decimals="2" :end-val="homeCount.today.profit" :duration="3200" class="card-panel-num" />
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="本月" name="month">
          <el-row :gutter="40" class="panel-group">
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="list" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    本月订单数
                  </div>
                  <count-to :start-val="0" :end-val="homeCount.month.orderCount" :duration="2600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('shoppings')">
                <div class="card-panel-icon-wrapper icon-shopping">
                  <svg-icon icon-class="shopping" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    本月销售额
                  </div>
                  <count-to :start-val="0" :decimals="2" :end-val="homeCount.month.saleCount" :duration="3600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('purchases')">
                <div class="card-panel-icon-wrapper icon-money">
                  <svg-icon icon-class="money" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    本月利润
                  </div>
                  <count-to :start-val="0" :decimals="2" :end-val="homeCount.month.profit" :duration="3200" class="card-panel-num" />
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="本年" name="year">
          <el-row :gutter="40" class="panel-group">
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="list" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    本年订单数
                  </div>
                  <count-to :start-val="0" :end-val="homeCount.year.orderCount" :duration="2600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('shoppings')">
                <div class="card-panel-icon-wrapper icon-shopping">
                  <svg-icon icon-class="shopping" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    本年销售额
                  </div>
                  <count-to :start-val="0" :decimals="2" :end-val="homeCount.year.saleCount" :duration="3600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('purchases')">
                <div class="card-panel-icon-wrapper icon-money">
                  <svg-icon icon-class="money" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    本年利润
                  </div>
                  <count-to :start-val="0" :decimals="2" :end-val="homeCount.year.profit" :duration="3200" class="card-panel-num" />
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="总" name="all">
          <el-row :gutter="40" class="panel-group">
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="list" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    总订单数
                  </div>
                  <count-to :start-val="0" :end-val="homeCount.all.orderCount" :duration="2600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('shoppings')">
                <div class="card-panel-icon-wrapper icon-shopping">
                  <svg-icon icon-class="shopping" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    总销售额
                  </div>
                  <count-to :start-val="0" :decimals="2" :end-val="homeCount.all.saleCount" :duration="3600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :md="8" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('purchases')">
                <div class="card-panel-icon-wrapper icon-money">
                  <svg-icon icon-class="money" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    总利润
                  </div>
                  <count-to :start-val="0" :decimals="2" :end-val="homeCount.all.profit" :duration="3200" class="card-panel-num" />
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-col>
    <el-col :xs="24" :md="6" style="padding-left: 20px">
      <el-row :gutter="40" class="panel-group">
        <el-col :span="24" class="card-panel-col">
          <div class="card-panel" @click="handleSetLineChartData('messages')">
            <div class="card-panel-icon-wrapper icon-message">
              <svg-icon icon-class="money" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                库存价值
              </div>
              <count-to :start-val="0" :decimals="2" :end-val="homeCount.inventoryValue" :duration="2600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
      </el-row>
    </el-col>
  </el-row>

</template>

<script>
import CountTo from 'vue-count-to'
import homeApi from '@/api/home'

export default {
  components: {
    CountTo
  },
  data() {
    return {
      homeCount: {
        today: {},
        month: {},
        year: {},
        all: {}
      },
      activeTab: 'today'
    }
  },
  created() {
    homeApi.getHomeCount().then(res => { this.homeCount = res.data })
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  // margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #f5d72e;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #f5d72e;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    float: none !important;
    margin-left: 50px !important;
    width: 100%;
    height: 100%;
  }

  .card-panel-icon-wrapper {
    display: none;
  }
}
</style>
