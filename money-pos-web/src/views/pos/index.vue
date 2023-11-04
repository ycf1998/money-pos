<template>
    <PageWrapper>
        <div class="flex flex-col-reverse md:flex-row gap-6">
            <div class="flex-1 grid gap-6">
                <el-card>
                    <div class="flex gap-10">
                        <div class="flex-1 flex flex-col gap-4">
                            <el-autocomplete
                                v-model="goodsSelect.barcode"
                                value-key="barcode"
                                placeholder="条码 or 名称"
                                clearable
                                :fetch-suggestions="goodsSelect.queryGoods"
                                @select="goodsSelect.selectGoods"
                                @keydown.enter="goodsSelect.enterGoods"
                            >
                                <template #default="{ item }">
                                    <div class="flex flex-col">
                                        <span>{{ item.barcode }}</span>
                                        <span>{{ item.name }} 🌰 {{ item.stock }}</span>
                                    </div>
                                </template>
                            </el-autocomplete>
                            <el-autocomplete
                                v-model="memberSelect.name"
                                value-key="name"
                                placeholder="会员名 or 手机号"
                                clearable
                                :fetch-suggestions="memberSelect.queryMember"
                                @select="memberSelect.selectMember"
                            >
                                <template #default="{ item }">
                                    <div class="flex flex-col">
                                        <span>{{ item.name }}</span>
                                        <span>{{ item.phone }} 🎫 {{ item.coupon }}</span>
                                    </div>
                                </template>
                                <template #suffix>
                                    <el-tag v-if="selectedMember">存券：{{ selectedMember.coupon }}</el-tag>
                                </template>
                            </el-autocomplete>
                        </div>
                        <div class="hidden md:block">
                            <el-button type="success" class="!h-full" @click="showOrder">
                                <h1>收款</h1>
                                <h4>（空格space）</h4>
                            </el-button>
                        </div>
                    </div>
                </el-card>
                <el-card>
                    <div class="flex justify-between mb-2">
                        <h4>共 {{ total }} 件</h4>
                        <h4>💵{{ totalAmount }} 🎫{{ couponAmount }} <span style="font-size:22px">💰{{ payAmount }}</span>
                        </h4>
                    </div>
                    <el-table :data="order" border class="w-full" flexible>
                        <el-table-column v-if="!tool.simple" align="center" prop="goodsBarcode" label="条码"
                                         max-width="150" />
                        <el-table-column align="center" :fixed="mobile" prop="goodsName" label="商品" max-width="180"
                                         min-width="100" />
                        <el-table-column align="center" prop="quantity" label="数量" width="120">
                            <template #default="{row, $index}">
                                <el-input v-model="row.quantity" class="!text-center"
                                          @change="changeQuantity(row, $index)">
                                    <template #prefix><span @click="row.quantity-- && changeQuantity(row, $index)"
                                                            class="cursor-pointer">-</span>
                                    </template>
                                    <template #suffix><span @click="row.quantity++ && changeQuantity(row, $index)"
                                                            class="cursor-pointer">+</span>
                                    </template>
                                </el-input>
                            </template>
                        </el-table-column>
                        <el-table-column v-if="!tool.simple" align="center" prop="salePrice" label="原价" width="100" />
                        <el-table-column v-if="!tool.simple" align="center" prop="vipPrice" label="会员价" width="100">
                            <template #default="{row}">
                                {{ tool.vip ? row.vipPrice : 0 }}
                            </template>
                        </el-table-column>
                        <el-table-column align="center" prop="coupon" label="购物券" width="100">
                            <template #default="{row}">
                                {{ tool.vip ? row.coupon : 0 }}
                            </template>
                        </el-table-column>
                        <el-table-column align="center" prop="goodsPrice" label="应收" width="120">
                            <template #default="{row}">
                                <el-input v-if="tool.editPrice" v-model="row.goodsPrice" class="edit-price-input">
                                    <template #append class="!px-2">
                                        <el-icon>
                                            <Refresh @click="cancelEditPrice(row)" />
                                        </el-icon>
                                    </template>
                                </el-input>
                                <span v-else>{{ row.goodsPrice }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column align="center" prop="subCount" label="小计" width="120">
                            <template #default="{row}">
                                {{ NP.times(row.quantity, row.goodsPrice) }}
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
            </div>
            <div class="flex-none md:w-40">
                <el-card>
                    <div class="grid grid-cols-3 md:grid-cols-none gap-3">
                        <el-button class="!h-14" plain @click="refresh">刷新</el-button>
                        <el-button class="!m-0 !h-14" plain :type="tool.vip ? 'success' : ''" @click="brushVip">
                            {{ tool.vip ? '取消会员' : '刷会员' }}
                        </el-button>
                        <el-button class="!m-0 !h-14" plain :type="tool.editPrice ? 'success' : ''" @click="editPrice">
                            {{ tool.editPrice ? '完成' : '修改价格' }}
                        </el-button>
                        <el-button class="!m-0 !h-14" plain @click="clearOrder">清空商品</el-button>
                        <el-button v-if="mobile" class="!m-0 !h-14" plain type="success" @click="showOrder">收款
                        </el-button>
                        <el-button v-if="mobile" class="!m-0 !h-14" plain :type="tool.simple ? 'success' : ''"
                                   @click="tool.simple = !tool.simple">精简
                        </el-button>
                    </div>
                </el-card>
            </div>
        </div>
        <el-dialog
            v-model="dialogVisible"
            title="清单"
            class="!w-11/12 md:!w-1/2 lg:!w-1/3"
        >
            <div class="flex flex-col gap-3">
                <div class="grid grid-cols-4  text-center text-base font-bold mb-3">
                    <span>商品名称</span>
                    <span>原价</span>
                    <span>现价</span>
                    <span>优惠</span>
                </div>
                <div v-for="(item, index) in order" :key="index" class="grid grid-cols-12 text-center gap-3">
                    <span class="col-span-3">{{ item.goodsName }}</span>
                    <span class="col-span-3">{{ item.salePrice }}</span>
                    <span class="col-span-3">{{ item.goodsPrice }}</span>
                    <span class="col-span-3">{{ tool.vip ? item.coupon : 0 }}</span>
                    <span class="col-span-4 font-light">数量 X {{ item.quantity }}</span>
                    <span class="col-span-4 font-light">小计 {{ NP.times(item.goodsPrice, item.quantity) }}</span>
                    <span class="col-span-4 font-light">优惠 {{ tool.vip ? NP.times(item.coupon, item.quantity) : 0
                        }}</span>
                </div>
                <div class="grid grid-cols-2 text-center text-base mt-3">
                    <span>总计：{{ totalAmount }}</span>
                    <span>优惠：{{ NP.minus(totalAmount, payAmount) }}</span>
                    <span>应付：{{ payAmount }}</span>
                    <span>用券：{{ couponAmount }}</span>
                    <span v-if="tool.vip && selectedMember.type !== 'INNER'">会员：{{ selectedMember.name
                        }}</span>
                    <span
                        v-if="tool.vip && selectedMember.type !== 'INNER'">余券：{{ NP.minus(selectedMember.coupon, couponAmount)
                        }}</span>
                </div>
            </div>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="settleAccounts">确认</el-button>
              </span>
            </template>
        </el-dialog>
        <print-order ref="printOrder" />
    </PageWrapper>
</template>
<script setup>
import PageWrapper from "@/components/PageWrapper.vue";
import PrintOrder from "@/views/pos/printOrder.vue";

import {computed, ref} from "vue";
import {ElMessage} from "element-plus";
import NP from "number-precision";
import {isMobile} from "@/utils/index.js";
import posApi from "@/api/pos/pos.js";

const mobile = isMobile()
const goods = ref([])
const members = ref([])
const innerMember = ref()
const dialogVisible = ref(false)
const loading = ref(null)
const printOrder = ref()
refresh()
const selectedMember = ref(null)
const order = ref([])
const tool = ref({
    vip: false,
    editPrice: false,
    simple: mobile
})

const total = computed(() => order.value.reduce((prev, next) => prev + (next.quantity | 0), 0))
const totalAmount = computed(() => order.value.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, next.salePrice)), 0))
const couponAmount = computed(() => order.value.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, tool.value.vip ? next.coupon : 0)), 0))
const payAmount = computed(() => order.value.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, next.goodsPrice)), 0))

document.onkeydown = (e) => {
    if (e.code === 'Space') {
        showOrder()
    }
}

const goodsSelect = ref({
    barcode: null,
    queryGoods: (keyword, cb) => {
        if (goods.value.length < 16 && (keyword === 'null' || keyword === null || keyword === '')) {
            cb(goods.value)
        } else {
            const filterGoods = goods.value.filter(e => e.barcode.includes(keyword) || e.name.includes(keyword))
            cb(filterGoods.length > 15 ? [] : filterGoods)
        }
    },
    selectGoods: (item) => {
        const o = order.value.find(e => e.goodsBarcode === item.barcode)
        if (o) {
            o.quantity += 1
        } else {
            order.value.push({
                goodsId: item.id,
                goodsBarcode: item.barcode,
                goodsName: item.name,
                quantity: 1,
                salePrice: item.salePrice,
                vipPrice: item.vipPrice,
                coupon: item.coupon,
                goodsPrice: tool.value.vip ? item.vipPrice : item.salePrice
            })
        }
        goodsSelect.value.barcode = null
    },
    enterGoods: () => {
        const target = goods.value.find(e => e.barcode === goodsSelect.value.barcode)
        if (target) goodsSelect.value.selectGoods(target)
    }
})
const memberSelect = ref({
    name: null,
    queryMember: (keyword, cb) => {
        if (members.value.length < 16 && (keyword === 'null' || keyword === null || keyword === '')) {
            cb(members.value)
        } else {
            const filterMembers = members.value.filter(e => e.name.includes(keyword) || e.phone.includes(keyword))
            cb(filterMembers.length > 15 ? [] : filterMembers)
        }
    },
    selectMember: (item) => {
        selectedMember.value = item
        tool.value.vip = true
        order.value.forEach(o => o.goodsPrice = o.vipPrice)
    }
})

/**
 * 修改数量
 * @param row
 * @param index
 */
function changeQuantity(row, index) {
    if (row.quantity <= 0) {
        order.value.splice(index, 1)
    }
}

/**
 * 显示订单
 */
function showOrder() {
    if (loading.value != null) return
    if (dialogVisible.value) {
        settleAccounts()
    } else {
        if (order.value.length > 0) {
            dialogVisible.value = true
        } else {
            ElMessage.warning('没有商品')
        }
    }
}

/**
 * 结算
 */
function settleAccounts() {
    loading.value = ElLoading.service({
        lock: true,
        text: '结算中',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    const data = {
        member: selectedMember.value ? selectedMember.value.id : null,
        orderDetail: order.value
    }
    posApi.settleAccounts(data)
        .then(res => {
            ElMessage.success('订单结算成功')
            dialogVisible.value = false
            const info = res.data
            if (info.vip) {
                selectedMember.value.coupon = NP.minus(selectedMember.value.coupon, info.couponAmount)
            }
            // 移动端不打印
            if (!mobile) {
                printOrder.value.print({
                    info: info,
                    detail: order.value,
                    member: selectedMember.value
                })
            }
            settleAccountsOk()
            loading.value.close()
            loading.value = null
        })
        .catch(() => {
            loading.value.close()
            loading.value = null
        })
}

/**
 * 结算成功
 */
function settleAccountsOk() {
    memberSelect.value.name = null
    selectedMember.value = null
    tool.value.vip = false
    tool.value.editPrice = false
    clearOrder()
    refresh()
}


/**
 * 刷新
 * @returns {Promise<void>}
 */
async function refresh() {
    goods.value = await posApi.listGoods().then(res => res.data)
    members.value = await posApi.listMember().then(res => res.data)
    innerMember.value = members.value.find(e => e.type === 'INNER')
}

/**
 * 刷会员
 */
function brushVip() {
    if (tool.value.vip) {
        selectedMember.value = null
        memberSelect.value.name = null
    } else {
        selectedMember.value = innerMember.value
        memberSelect.value.name = innerMember.value.name
    }
    tool.value.vip = !tool.value.vip
    order.value.forEach(o => o.goodsPrice = o.vipPrice)
}

/**
 * 修改商品价格
 */
function editPrice() {
    tool.value.editPrice = !tool.value.editPrice
    order.value.forEach(o => o.oldGoodsPrice = o.goodsPrice)
}

/**
 * 取消当前价格修改
 * @param row
 */
function cancelEditPrice(row) {
    row.goodsPrice = row.oldGoodsPrice
}

/**
 * 清空商品
 */
function clearOrder() {
    order.value = []
}
</script>
<style>
.el-autocomplete input {
    padding: 1.25rem 0;
    font-size: 18px;
}

.edit-price-input .el-input-group__append {
    padding: 0 5px;
    cursor: pointer;
}
</style>