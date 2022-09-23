<template>
  <div class="app-container">
    <el-row :gutter="10">
      <!-- 工具栏 -->
      <el-col :sm="4" :md="3" :lg="2" style="float: right; margin-bottom: 10px;">
        <el-card shadow="always">
          <div class="tool">
            <el-button plain @click="reload">刷新</el-button>
            <el-button :type="isVip ? 'success' : ''" plain @click="brushVip">
              {{ isVip ? '取消会员' : '刷会员' }}
            </el-button>
            <el-button :type="tool.editPrice ? 'success' : ''" plain @click="editPrice">修改价格</el-button>
            <el-button plain @click="clearOrderList">清空商品</el-button>
            <el-button v-if="isMobile" type="success" plain @click="showOrder">收款</el-button>
            <el-button v-if="isMobile" plain @click="simple = !simple">精简</el-button>
          </div>
        </el-card>
      </el-col>
      <!-- 收银台 -->
      <el-col class="main" :sm="20" :md="21" :lg="22">
        <!-- 输入 -->
        <div class="cashier-input">
          <el-card shadow="always">
            <el-row :gutter="10">
              <el-col :sm="17" :md="18" :lg="19">
                <div>
                  <el-autocomplete v-model="barcode" class="cashier-input-item" popper-class="cashier-input-item" :fetch-suggestions="queryGoods" placeholder="条码 or 名称" @select="item => barcode = item.barcode" @keydown.enter.native="enterBarcode">
                    <template slot-scope="{ item }">
                      <div class="label">{{ item.barcode }}</div>
                      <span class="desc">{{ item.name }}
                        <svg-icon icon-class="stock" /> {{ item.stock }}
                      </span>
                    </template>
                  </el-autocomplete>
                </div>
                <div>
                  <el-autocomplete v-model="member" class="cashier-input-item" popper-class="cashier-input-item" :fetch-suggestions="queryMember" placeholder="会员名 or 手机号" @select="item => member = item.name" @keydown.enter.native="enterMember">
                    <template slot-scope="{ item }">
                      <div class="label">{{ item.name }}</div>
                      <span class="desc">{{ item.phone }}
                        <svg-icon icon-class="coupon" /> {{ item.coupon }}
                      </span>
                    </template>
                  </el-autocomplete>
                  <p v-if="currentMember" style="font-size: 13px">存券：{{ currentMember.coupon }}</p>
                </div>
              </el-col>
              <el-col :xs="0" :offset="2" :sm="5" :md="4" :lg="3">
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
                <h4>共{{ total }}件</h4>
              </el-col>
              <el-col align="right">
                <h4>💰{{ totalAmount }}
                  <svg-icon icon-class="coupon" /> {{ couponAmount }} <span style="font-size:20px">🪙{{ payAmount
                  }}</span>
                </h4>
              </el-col>
            </el-row>
            <el-table ref="table" border :data="orderList" style="width: 100%;" row-key="goodsBarcode">
              <template v-if="simple">
                <el-table-column v-if="false" key="1" prop="goodsBarcode" align="center" label="条码" />
                <el-table-column key="2" prop="goodsName" align="center" label="商品" />
                <el-table-column key="3" prop="quantity" align="center" label="数量">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.quantity" @change="changeQuantity(scope.row)" />
                  </template>
                </el-table-column>
                <el-table-column v-if="false" key="4" prop="salePrice" align="center" label="原价" />
                <el-table-column v-if="false" key="5" prop="vipPrice" align="center" label="会员价">
                  <template slot-scope="scope">
                    {{ isVip ? scope.row.vipPrice : 0 }}
                  </template>
                </el-table-column>
                <el-table-column key="6" prop="coupon" align="center" label="购物券">
                  <template slot-scope="scope">
                    {{ isVip ? scope.row.coupon : 0 }}
                  </template>
                </el-table-column>
                <el-table-column key="7" prop="goodsPrice" align="center" label="应收">
                  <template slot-scope="scope">
                    <template v-if="tool.editPrice">
                      <el-input v-model="scope.row.goodsPrice" placeholder="请输入内容" class="input-with-select" />
                    </template>
                    <span v-else style="font-size: 16px">{{ scope.row.goodsPrice }}</span>
                  </template>
                </el-table-column>
                <el-table-column key="8" prop="subcount" align="center" label="小计">
                  <template slot-scope="scope">
                    <span style="font-size: 18px">{{ calculator.Mul(scope.row.quantity, scope.row.goodsPrice) }}</span>
                  </template>
                </el-table-column>
              </template>
              <template v-else>
                <el-table-column key="1" prop="goodsBarcode" align="center" label="条码" />
                <el-table-column key="2" prop="goodsName" align="center" label="商品" />
                <el-table-column key="3" prop="quantity" align="center" label="数量">
                  <template slot-scope="scope">
                    <el-input-number v-model="scope.row.quantity" size="small" :min="0" @change="changeQuantity(scope.row)" />
                  </template>
                </el-table-column>
                <el-table-column key="4" prop="salePrice" align="center" label="原价" />
                <el-table-column key="5" prop="vipPrice" align="center" label="会员价">
                  <template slot-scope="scope">
                    {{ isVip ? scope.row.vipPrice : 0 }}
                  </template>
                </el-table-column>
                <el-table-column key="6" prop="coupon" align="center" label="购物券">
                  <template slot-scope="scope">
                    {{ isVip ? scope.row.coupon : 0 }}
                  </template>
                </el-table-column>
                <el-table-column key="7" prop="goodsPrice" align="center" label="应收">
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
              </template>
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
      simple: false,
      calculator: calculator,
      orderList: [],
      goods: [],
      members: [],
      barcode: null,
      member: null,
      currentMember: null,
      isVip: false,
      tool: {
        editPrice: false
      },
      showOrderDialog: false,
      fullscreenLoading: false
    }
  },
  computed: {
    total: function() {
      return this.orderList.reduce(
        (prev, next) => (prev.quantity | 0) + (next.quantity | 0),
        0
      )
    },
    totalAmount: function() {
      let val = 0.0
      this.orderList.forEach((data) => {
        val += calculator.Mul(data.quantity, data.salePrice)
      })
      return val
    },
    payAmount: function() {
      let val = 0.0
      this.orderList.forEach((data) => {
        val += calculator.Mul(data.quantity, data.goodsPrice)
      })
      return val
    },
    couponAmount: function() {
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
    // 空格收银
    const that = this
    document.onkeydown = function(e) {
      const key = window.event.keyCode
      if (key === 32) {
        e.preventDefault()
        that.showOrder()
      }
    }
    this.loadInit()
  },
  methods: {
    // 初始 or 刷新
    loadInit() {
      posApi.listMember(this.member).then((res) => {
        this.members = res.data
      })
      posApi.listGoods(this.barcode).then((res) => {
        this.goods = res.data
      })
    },
    // 查询商品
    queryGoods(barcodeOrName, cb) {
      let results = barcodeOrName
        ? this.goods.filter(
          (e) =>
            e.barcode.includes(barcodeOrName) ||
              e.name.includes(barcodeOrName)
        )
        : this.goods
      if (results.length > 10) {
        results = []
      }
      cb(results)
    },
    // 查询会员
    queryMember(nameOrPhone, cb) {
      let results = nameOrPhone
        ? this.members.filter(
          (e) => e.name.includes(nameOrPhone) || e.phone.includes(nameOrPhone)
        )
        : this.members
      if (results.length > 15) {
        results = []
      }
      cb(results)
    },
    // 回车商品
    enterBarcode(e) {
      if (!this.barcode || (this.barcode && this.barcode.length < 1)) return
      const goods = this.goods.find((e) => e.barcode === this.barcode)
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
    enterMember(e) {
      if (!this.member || (this.member && this.member.length < 1)) return
      const member = this.members.find((e) => e.name === this.member)
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
      // 为0删除商品
      if (goods.quantity === 0) {
        console.log(this.orderList)
        this.orderList = this.orderList.filter(
          (e) => e.goodsId !== goods.goodsId
        )
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
              return [
                Object.assign({ key: Math.random() }, o),
                Object.assign({ key: Math.random() }, o)
              ]
            }),
            member: Object.assign({}, this.currentMember)
          }
          this.settleAccountsOk()
          this.fullscreenLoading = false
          // 移动端不打印
          if (!isMobile) {
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
        this.member = ''
        this.currentMember = null
        this.orderList.forEach((e) => {
          e.goodsPrice = e.salePrice
        })
      } else if (!this.currentMember) {
        // 默认刷内部号
        const member = this.members.find((e) => e.type === 'INNER')
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
.main {
  // height: 100vh;
  display: flex;
  flex-direction: column;

  .cashier-input {
    margin-bottom: 10px;
  }
}

.el-autocomplete-suggestion li.highlighted,
.el-autocomplete-suggestion li:hover {
  background-color: lightgray;
}

.cashier-input-item {
  width: 100%;
  padding: 7px;

  input {
    font-size: 14px;
    height: 42px !important;
    line-height: 42px !important;
  }

  li {
    line-height: normal;
    padding: 7px;

    .label {
      text-overflow: ellipsis;
      overflow: hidden;
    }

    .desc {
      font-size: 12px;
      color: #b4b4b4;
    }
  }
}

.tool {
  display: flex;
  flex-direction: column;

  .el-button {
    margin-top: 10px;
    padding: 18px 0;
  }

  .el-button + .el-button {
    margin-left: 0;
  }
}

.orderDialog {
  .el-dialog {
    width: 100%;
    max-width: 450px;
  }
}
</style>
