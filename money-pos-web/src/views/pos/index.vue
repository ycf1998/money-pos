<template>
  <div class="app-container">
    <el-row :gutter="10">
      <!-- 工具栏 -->
      <el-col :sm="4" :md="3" :lg="2" style="float: right; margin-bottom: 10px;">
        <el-card class="tool-card" shadow="always">
          <div class="tool-list">
            <el-button plain @click="reload">刷新</el-button>
            <el-button plain :type="isVip ? 'success' : ''" @click="brushVip">{{ isVip ? '取消会员' : '刷会员' }}</el-button>
            <el-button plain :type="tool.editPrice ? 'success' : ''" @click="editPrice">修改价格</el-button>
            <el-button plain @click="clearOrderList">清空商品</el-button>
            <el-button v-if="isMobile" type="success" @click="showOrder">收款</el-button>
            <el-button v-if="isMobile" plain :type="tool.simple ? 'success' : ''" @click="tool.simple = !tool.simple">精简</el-button>
          </div>
        </el-card>
      </el-col>
      <!-- 收银台 -->
      <el-col class="cashier" :sm="20" :md="21" :lg="22">
        <!-- 输入 -->
        <div class="cashier-input">
          <el-card shadow="always">
            <el-row :gutter="40" type="flex" class="row-bg" justify="space-between" align="middle">
              <el-col :xs="24" :sm="17" :md="19" :lg="20">
                <el-row type="flex" class="row-bg">
                  <el-autocomplete v-model="barcode" class="cashier-input-item" popper-class="cashier-input-item" :fetch-suggestions="queryGoods" placeholder="条码 or 名称" @select="item => barcode = item.barcode" @keydown.enter.native="enterBarcode">
                    <template slot-scope="{ item }">
                      <span class="label">{{ item.barcode }}</span>
                      <span class="desc">{{ item.name }} 🌰 {{ item.stock }}</span>
                    </template>
                  </el-autocomplete>
                </el-row>
                <el-row type="flex" class="row-bg" align="middle">
                  <el-autocomplete v-model="member" class="cashier-input-item" popper-class="cashier-input-item" :fetch-suggestions="queryMember" placeholder="会员名 or 手机号" @select="item => member = item.name" @keydown.enter.native="enterMember">
                    <template slot-scope="{ item }">
                      <span class="label">{{ item.name }}</span>
                      <span class="desc">{{ item.phone }} 🎫 {{ item.coupon }}</span>
                    </template>
                  </el-autocomplete>
                  <el-tag v-if="currentMember">存券：{{ currentMember.coupon }}</el-tag>
                </el-row>
              </el-col>
              <el-col :xs="0" :sm="7" :md="5" :lg="4">
                <el-button type="success" style="width:100%;height:100%" @click="showOrder">
                  <h1>收款</h1>
                  <h4>（空格space）</h4>
                </el-button>
              </el-col>
            </el-row>
          </el-card>
        </div>
        <!-- 商品列表 -->
        <div class="cashier-goods">
          <el-card shadow="always">
            <el-row type="flex" justify="between">
              <el-col>
                <h4>共 {{ total }} 件</h4>
              </el-col>
              <el-col align="right">
                <h4>💰{{ totalAmount }} 🎫 {{ couponAmount }} <span style="font-size:20px">🪙{{ payAmount }}</span>
                </h4>
              </el-col>
            </el-row>
            <el-table ref="table" border :data="orderList" style="width: 100%;" fit row-key="goodsBarcode">
              <el-table-column v-if="!tool.simple" key="1" prop="goodsBarcode" min-width="120" align="center" label="条码" />
              <el-table-column key="2" :fixed="tool.simple" prop="goodsName" min-width="120" align="center" label="商品" />
              <el-table-column key="3" prop="quantity" align="center" label="数量">
                <template slot-scope="scope">
                  <el-input-number v-if="!tool.simple" v-model="scope.row.quantity" size="small" :min="0" @change="changeQuantity(scope.row)" />
                  <el-input v-else v-model="scope.row.quantity" @change="changeQuantity(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column v-if="!tool.simple" key="4" prop="salePrice" align="center" label="原价" />
              <el-table-column v-if="!tool.simple" key="5" prop="vipPrice" align="center" label="会员价">
                <template slot-scope="scope">
                  {{ isVip ? scope.row.vipPrice : 0 }}
                </template>
              </el-table-column>
              <el-table-column key="6" prop="coupon" align="center" label="购物券">
                <template slot-scope="scope">
                  {{ isVip ? scope.row.coupon : 0 }}
                </template>
              </el-table-column>
              <el-table-column key="7" :min-width="tool.editPrice ? 120 : 0" prop="goodsPrice" align="center" label="应收">
                <template slot-scope="scope">
                  <template v-if="tool.editPrice">
                    <el-input v-model="scope.row.goodsPrice" placeholder="请输入内容" class="input-with-select">
                      <el-button slot="append" icon="el-icon-refresh" @click="cancelEdit(scope.row)" />
                    </el-input>
                  </template>
                  <span v-else style="font-size: 16px">{{ scope.row.goodsPrice }}</span>
                </template>
              </el-table-column>
              <el-table-column key="8" prop="subcount" align="center" label="小计">
                <template slot-scope="scope">
                  <span style="font-size: 18px">{{ calculator.Mul(scope.row.quantity, scope.row.goodsPrice) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <!-- 清单 -->
    <el-dialog v-loading.fullscreen.lock="fullscreenLoading" class="orderDialog" title="清单" :visible.sync="showOrderDialog">
      <el-row :gutter="10" style="text-align: center">
        <el-col :span="6">
          <h4>商品名称</h4>
        </el-col>
        <el-col :span="6">
          <h4>原价</h4>
        </el-col>
        <el-col :span="6">
          <h4>现价</h4>
        </el-col>
        <el-col :span="6">
          <h4>优惠</h4>
        </el-col>
      </el-row>
      <div v-for="item in orderList" :key="item.barcode">
        <el-row style="text-align: center;margin-bottom: 10px;">
          <el-col :span="6"><span>{{ item.goodsName }}</span></el-col>
          <el-col :span="6"><span>{{ item.salePrice }}</span></el-col>
          <el-col :span="6"><span>{{ item.goodsPrice }}</span></el-col>
          <el-col :span="6"><span>{{ isVip ? item.coupon : 0 }}</span></el-col>
        </el-row>
        <el-row type="flex" style="text-align: center;margin-bottom: 20px;">
          <el-col><span style="color: #909399">数量 X {{ item.quantity }}</span></el-col>
          <el-col><span style="color: #909399">小计{{ calculator.Mul(item.goodsPrice, item.quantity) }}元</span></el-col>
          <el-col><span style="color: #909399">优惠{{ isVip ? calculator.Mul(item.coupon, item.quantity) : 0 }}元</span>
          </el-col>
        </el-row>
      </div>
      <el-row style="text-align: center;margin-bottom: 10px;font-size: 15px">
        <el-col :span="12">总计：{{ totalAmount }}</el-col>
        <el-col :span="12">优惠：{{ calculator.Sub(totalAmount, payAmount) }}</el-col>
        <el-col :span="12">应付：{{ payAmount }}</el-col>
        <el-col :span="12">用券：{{ couponAmount }}</el-col>
      </el-row>
      <el-row v-if="isMobile && currentMember" style="text-align: center;margin-bottom: 10px;font-size: 15px">
        <el-col :span="12">余券：{{ calculator.Sub(currentMember.coupon, couponAmount) }}</el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showOrderDialog = false"> 取消 </el-button>
        <el-button type="primary" @click="settleAccounts()"> 提交 </el-button>
      </div>
    </el-dialog>

    <div style="display:none">
      <print-order ref="child" :is-pos="true" />
    </div>
  </div>
</template>

<script>
import { isMobile } from '@/utils/index'
import posApi from '@/api/pos/pos'
import calculator from '@/utils/calculator'
import printOrder from '../oms/order/printOrder.vue'

export default {
  components: { printOrder },
  data() {
    return {
      isMobile: isMobile(),
      calculator: calculator,

      goodsList: [],
      memberList: [],

      barcode: null,
      member: null,
      orderList: [],

      currentMember: null,
      isVip: false,
      tool: {
        simple: false,
        editPrice: false
      },
      showOrderDialog: false,
      fullscreenLoading: false
    }
  },
  computed: {
    total: function () {
      return this.orderList.reduce((prev, next) => (prev.quantity | 0) + (next.quantity | 0), 0)
    },
    totalAmount: function () {
      let val = 0.0
      this.orderList.forEach((data) => {
        val += calculator.Mul(data.quantity, data.salePrice)
      })
      return val
    },
    payAmount: function () {
      let val = 0.0
      this.orderList.forEach((data) => {
        val += calculator.Mul(data.quantity, data.goodsPrice)
      })
      return val
    },
    couponAmount: function () {
      let val = 0.0
      if (this.isVip) {
        this.orderList.forEach((data) => {
          val += calculator.Mul(data.quantity, data.coupon)
        })
      }
      return val
    }
  },
  created() {
    this.tool.simple = this.isMobile
    // 空格收银
    const that = this
    document.onkeydown = function (e) {
      const key = window.event.keyCode
      if (key === 32) {
        e.preventDefault()
        that.showOrder()
      }
    }
    // 初始化
    this.loadInit()
  },
  methods: {
    // 初始 or 刷新
    loadInit() {
      posApi.listGoods(this.barcode).then((res) => {
        this.goodsList = res.data
      })
      posApi.listMember(this.member).then((res) => {
        this.memberList = res.data
      })
    },
    // 查询商品
    queryGoods(barcodeOrName, cb) {
      let result = this.goodsList
      if (barcodeOrName) {
        result = this.goodsList.filter((e) => e.barcode.includes(barcodeOrName) || e.name.includes(barcodeOrName))
      }
      cb(result.length > 10 ? [] : result)
    },
    // 查询会员
    queryMember(nameOrPhone, cb) {
      let result = this.memberList
      if (nameOrPhone) {
        result = this.memberList.filter((e) => e.name.includes(nameOrPhone) || e.phone.includes(nameOrPhone))
      }
      cb(result.length > 10 ? [] : result)
    },
    // 回车商品
    enterBarcode() {
      if (!this.barcode?.length > 0) return
      const goods = this.goodsList.find((e) => e.barcode === this.barcode)
      if (goods) {
        let orderDetail = this.orderList.find((e) => e.goodsId === goods.id)
        if (!orderDetail) {
          orderDetail = {
            goodsId: goods.id,
            goodsBarcode: goods.barcode,
            goodsName: goods.name,
            quantity: 1,
            salePrice: goods.salePrice,
            vipPrice: goods.vipPrice,
            coupon: goods.coupon,
            goodsPrice: this.isVip ? goods.vipPrice : goods.salePrice
          }
          this.orderList.push(orderDetail)
        } else {
          orderDetail.quantity += 1
        }
        this.barcode = ''
      } else {
        this.$notify({
          title: 'Warning',
          message: '未找到该商品',
          type: 'warning',
          duration: 2000
        })
      }
    },
    // 回车会员
    enterMember() {
      if (!this.member?.length > 0) return
      const member = this.memberList.find((e) => e.name === this.member)
      if (member) {
        this.currentMember = member
        this.isVip = true
        this.orderList.forEach((e) => {
          e.goodsPrice = e.vipPrice
        })
      }
    },
    // 修改数量
    changeQuantity(goods) {
      if (goods.quantity === 0) {
        this.orderList = this.orderList.filter((e) => e.goodsId !== goods.goodsId)
      }
    },
    // 显示订单
    showOrder() {
      if (this.showOrderDialog === true) {
        if (this.fullscreenLoading === false) {
          this.settleAccounts()
        }
      } else {
        if (this.orderList.length === 0) {
          this.$notify({
            title: 'Warning',
            message: '没有商品',
            type: 'warning',
            duration: 2000
          })
        } else {
          this.showOrderDialog = true
        }
      }
    },
    // 结算
    settleAccounts() {
      this.fullscreenLoading = true
      const data = {
        member: this.currentMember?.id,
        orderDetail: this.orderList
      }
      posApi
        .settleAccounts(data)
        .then((res) => {
          this.$notify({
            title: 'Success',
            message: '订单结算成功',
            type: 'success',
            duration: 2000
          })
          this.showOrderDialog = false
          const printOrderInfo = {
            info: res.data,
            detail: this.orderList.flatMap((o) => {
              return [Object.assign({ key: Math.random() }, o), Object.assign({ key: Math.random() }, o)]
            }),
            member: Object.assign({}, this.currentMember)
          }
          this.settleAccountsOk()
          this.fullscreenLoading = false
          // 移动端不打印
          if (!this.isMobile) {
            this.$refs.child.print(printOrderInfo)
          }
        })
        .catch(() => {
          this.fullscreenLoading = false
        })
    },
    // 结算成功，清理打印
    settleAccountsOk() {
      this.barcode = null
      this.member = null
      this.currentMember = null
      this.isVip = false
      this.tool.editPrice = false
      this.clearOrderList()
      this.loadInit()
    },
    // ============================ 工具 ===============================
    // 刷新
    reload() {
      this.loadInit()
    },
    // 刷会员
    brushVip() {
      this.isVip = !this.isVip
      if (!this.isVip) {
        this.member = null
        this.currentMember = null
        this.orderList.forEach((e) => {
          e.goodsPrice = e.salePrice
        })
      } else if (!this.currentMember) {
        // 默认刷内部号
        const member = this.memberList.find((e) => e.type === 'INNER')
        this.member = member.name
        this.currentMember = member
        this.orderList.forEach((e) => {
          e.goodsPrice = e.vipPrice
        })
      }
    },
    // 清空商品
    clearOrderList() {
      this.orderList = []
    },
    // 修改价格
    editPrice() {
      this.tool.editPrice = !this.tool.editPrice
      this.orderList.forEach((e) => {
        e.oldGoodsPrice = e.goodsPrice
      })
    },
    // 取消修改
    cancelEdit(goods) {
      goods.goodsPrice = goods.oldGoodsPrice
    }
  }
}
</script>

<style lang="scss">
.cashier {
  display: flex;
  flex-direction: column;
}

.orderDialog {
  .el-dialog {
    width: 100%;
    max-width: 450px;
  }
}

// 输入部分
.cashier-input {
  margin-bottom: 10px;
}

.cashier-input-item {
  width: 90%;
  padding: 7px;

  input {
    font-size: 14px;
    height: 42px !important;
    line-height: 42px !important;
  }

  li {
    display: flex;
    padding: 5px 20px;
    flex-direction: column;
    line-height: 20px;

    .label {
      font-size: 15px;
    }

    .desc {
      font-size: 13px;
      color: #999;
    }
  }
}

@media only screen and (max-width: 767px) {
  .cashier-input {
    .cashier-input-item {
      width: 100%;
    }
  }
}

// tool
.tool-card {
  .el-card__body {
    padding: 0.5rem !important;
  }
  .tool-list {
    display: flex;
    flex-direction: column;
    align-items: center;

    .el-button {
      width: 100px;
      height: 3rem;
      margin: 0.3rem;
    }
  }
}

@media only screen and (max-width: 767px) {
  .tool-card {
    .el-card__body {
      padding: 0.5rem !important;
    }
    .tool-list {
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      flex-wrap: wrap;

      .el-button {
        width: 100px;
        height: 3rem;
        margin: 0.3rem;
      }
    }
  }
}

.el-autocomplete-suggestion li.highlighted,
.el-autocomplete-suggestion li:hover {
  background-color: lightgray;
}
</style>
