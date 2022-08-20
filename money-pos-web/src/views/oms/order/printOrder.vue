<template>
  <div id="printTarget" style="width: 58mm;z-index: -1">
    <h1 style="text-align:center">麦尼收银</h1>
    <p style="font-size: 15px">单号：{{ order.info.orderNo }}</p>
    <p style="font-size: 15px">时间：{{ order.info.createTime }}</p>
    <table style="text-align: center;width:57mm;font-size:1.8mm;padding:1mm">
      <thead>
        <th>原价</th>
        <th>现价</th>
        <th>优惠</th>
        <th>小计</th>
      </thead>
      <tbody style="font-size:1.2mm">
        <tr v-for="(item, index) in order.detail" :key="item.key">
          <td v-if="index%2===0" colspan="2" style="text-align: left;">{{ item.goodsName }}</td>
          <td v-if="index%2===0" colspan="2" style="text-align: right;padding-right:4.5mm">X{{ item.quantity }}</td>
          <td v-if="index%2===1">{{ item.salePrice }}</td>
          <td v-if="index%2===1">{{ item.goodsPrice }}</td>
          <td v-if="index%2===1">{{ order.info.vip ? item.coupon : 0 }}</td>
          <td v-if="index%2===1">{{ calculator.Mul(item.goodsPrice, item.quantity) }}</td>
        </tr>
      </tbody>
    </table>
    <div style="text-align: center;width:58mm;">---------------------------------------</div>
    <table style="text-align: center;width:58mm;font-size:2mm;margin-top:3mm">
      <tbody>
        <tr>
          <td>总计：{{ order.info.totalAmount }}</td>
          <td>优惠：{{ calculator.Sub(order.info.totalAmount, order.info.payAmount) }}</td>
        </tr>
        <tr>
          <td>应付：{{ order.info.payAmount }}</td>
          <td>用券：{{ order.info.vip ? order.info.couponAmount : 0 }}</td>
        </tr>
        <tr v-if="order.info.vip && order.member.type !== 'INNER'">
          <td>会员：{{ order.member.name }}</td>
          <td>余券：{{ isPos ? calculator.Sub(order.member.coupon, order.info.couponAmount) : order.member.coupon }}</td>
        </tr>
        <tr>
          <td colspan="2">联系电话：{{ user.phone }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import calculator from '@/utils/calculator'
// eslint-disable-next-line no-unused-vars
import print from 'print-js'
export default {
  name: 'PrintOrder',
  props: {
    isPos: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      calculator: calculator,
      order: {
        info: {},
        detail: [],
        member: {}
      }
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  methods: {
    print(order) {
      this.order = order
      this.$nextTick(function() {
        const style = '@page {margin:0 10mm};'
        // eslint-disable-next-line no-undef
        printJS({
          printable: 'printTarget', // 标签元素id
          type: 'html',
          header: '',
          targetStyles: ['*'],
          style
        })
      })
    }
  }
}
</script>
