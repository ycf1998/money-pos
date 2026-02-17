<template>
    <PageWrapper>
        <!-- 节日装饰 -->
        <FestivalDecor
            v-if="festivalTheme.enabled.value"
            :festival="festivalTheme.currentFestival.value"
            :show-greeting="festivalTheme.showGreeting.value"
            @dismiss="festivalTheme.dismissGreeting"
            @test="festivalTheme.setTestFestival"
        />
        <!-- 主容器：移动端垂直反向排列，桌面端水平排列 -->
        <div class="pos-container">
            <!-- 左侧主区域 -->
            <div class="pos-main">
                <!-- 搜索区域 -->
                <el-card class="search-card">
                    <div class="search-wrapper">
                        <!-- 输入框区域 -->
                        <div class="search-inputs">
                            <el-autocomplete v-model="goodsSelect.barcode" value-key="barcode" placeholder="条码 or 名称"
                                clearable size="large" :fetch-suggestions="goodsSelect.queryGoods"
                                @select="goodsSelect.selectGoods" @keydown.enter="goodsSelect.enterGoods">
                                <template #prefix>
                                    <el-icon><Search /></el-icon>
                                </template>
                                <template #default="{ item }">
                                    <div class="suggestion-item">
                                        <div class="suggestion-info">
                                            <span class="font-medium">{{ item.barcode }}</span>
                                            <span class="text-xs el-text--secondary">{{ item.name }}</span>
                                        </div>
                                        <el-tag size="small" type="info">库存: {{ item.stock }}</el-tag>
                                    </div>
                                </template>
                            </el-autocomplete>
                            <div class="member-input-wrapper">
                                <el-autocomplete v-model="memberSelect.name" value-key="name" placeholder="会员名 or 手机号"
                                    clearable size="large" :fetch-suggestions="memberSelect.queryMember"
                                    @select="memberSelect.selectMember" @clear="clearMember">
                                    <template #prefix>
                                        <el-icon><User /></el-icon>
                                    </template>
                                    <template #default="{ item }">
                                        <div class="suggestion-item">
                                            <div class="suggestion-info">
                                                <span class="font-medium">{{ item.name }}</span>
                                                <span class="text-xs el-text--secondary">{{ item.phone }}</span>
                                            </div>
                                            <el-tag size="small" type="warning">存券: {{ item.coupon }}</el-tag>
                                        </div>
                                    </template>
                                    <template #suffix>
                                        <!-- 会员存券标签 -->
                                        <el-tag v-if="selectedMember" type="success" size="small">
                                            存券：{{ selectedMember.coupon }}
                                        </el-tag>
                                    </template>
                                </el-autocomplete>
                            </div>
                        </div>
                        <!-- 收款按钮（桌面端） -->
                        <div class="checkout-wrapper">
                            <el-button type="success" size="large" class="checkout-btn" @click="showOrder">
                                <div class="checkout-content">
                                    <div class="checkout-main">
                                        <el-icon class="text-xl mr-2">
                                            <Check />
                                        </el-icon>
                                        <span class="text-2xl font-bold mr-3">收款</span>
                                    </div>
                                    <kbd class="checkout-shortcut">空格</kbd>
                                </div>
                            </el-button>
                        </div>
                    </div>
                </el-card>

                <!-- 订单区域 -->
                <el-card class="order-card">
                    <template #header>
                        <div class="order-header">
                            <div class="order-header-left">
                                <h4 class="text-lg font-bold">购物清单</h4>
                                <el-tag type="info" size="small">共 {{ total }} 件</el-tag>
                            </div>
                            <div class="order-header-right">
                                <div class="summary-item">
                                    <el-icon class="text-yellow-500">
                                        <Coin />
                                    </el-icon>
                                    <span class="font-medium">{{ totalAmount }}</span>
                                    <span class="el-text--secondary">原价</span>
                                </div>
                                <div class="summary-item">
                                    <el-icon class="text-blue-500">
                                        <Ticket />
                                    </el-icon>
                                    <span class="font-medium">{{ couponAmount }}</span>
                                    <span class="el-text--secondary">用券</span>
                                </div>
                                <div class="summary-item highlight">
                                    <el-icon class="text-green-500">
                                        <Money />
                                    </el-icon>
                                    <span class="font-bold text-green-600">{{ payAmount }}</span>
                                    <span class="el-text--secondary">应收</span>
                                </div>
                            </div>
                        </div>
                    </template>

                    <template v-if="order.length > 0">
                        <el-table :data="order" class="w-full" stripe>
                            <el-table-column v-if="!tool.simple" align="center" prop="goodsBarcode" label="条码"
                                min-width="120" />
                            <el-table-column align="center" :fixed="mobile" prop="goodsName" label="商品"
                                min-width="120" />
                            <el-table-column align="center" prop="quantity" label="数量" width="130">
                                <template #default="{ row, $index }">
                                    <div class="quantity-control">
                                        <el-button size="small" circle
                                            @click="row.quantity-- && changeQuantity(row, $index)">
                                            <el-icon>
                                                <Minus />
                                            </el-icon>
                                        </el-button>
                                        <el-input v-model="row.quantity" class="w-12" size="small"
                                            @change="changeQuantity(row, $index)" />
                                        <el-button size="small" circle
                                            @click="row.quantity++ && changeQuantity(row, $index)">
                                            <el-icon>
                                                <Plus />
                                            </el-icon>
                                        </el-button>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column v-if="!tool.simple" align="center" prop="salePrice" label="原价"
                                width="80" />
                            <el-table-column v-if="!tool.simple" align="center" prop="vipPrice" label="会员价" width="80">
                                <template #default="{ row }">
                                    <span :class="tool.vip ? 'text-purple-500 font-medium' : 'el-text--placeholder'">
                                        {{ tool.vip ? row.vipPrice : '-' }}
                                    </span>
                                </template>
                            </el-table-column>
                            <el-table-column align="center" prop="coupon" label="购物券" width="80">
                                <template #default="{ row }">
                                    <span :class="tool.vip ? 'text-blue-500 font-medium' : 'el-text--placeholder'">
                                        {{ tool.vip ? row.coupon : '-' }}
                                    </span>
                                </template>
                            </el-table-column>
                            <el-table-column align="center" prop="goodsPrice" label="应收" width="100">
                                <template #default="{ row }">
                                    <el-input v-if="tool.editPrice" v-model="row.goodsPrice" size="small" class="w-20">
                                        <template #suffix>
                                            <el-icon class="cursor-pointer hover:text-red-500"
                                                @click="cancelEditPrice(row)">
                                                <Refresh />
                                            </el-icon>
                                        </template>
                                    </el-input>
                                    <span v-else class="text-green-600 font-bold">{{ row.goodsPrice }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column align="center" prop="subCount" label="小计" width="90">
                                <template #default="{ row }">
                                    <span class="font-bold">{{ NP.times(row.quantity, row.goodsPrice) }}</span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </template>
                    <el-empty v-else description="暂无商品" :image-size="80">
                        <template #description>
                            <p class="el-text--secondary">扫描商品条码开始购物</p>
                        </template>
                    </el-empty>
                </el-card>
            </div>

            <!-- 右侧工具栏 -->
            <div class="pos-sidebar">
                <el-card class="tool-card">
                    <div class="tool-grid">
                        <el-button class="tool-btn" plain @click="refresh">
                            <el-icon class="mr-1">
                                <Refresh />
                            </el-icon>
                            刷新
                        </el-button>
                        <el-button class="tool-btn" plain :type="tool.vip ? 'success' : ''" @click="brushVip">
                            <el-icon class="mr-1">
                                <User />
                            </el-icon>
                            {{ tool.vip ? '取消会员' : '刷会员' }}
                        </el-button>
                        <el-button class="tool-btn" plain :type="tool.editPrice ? 'success' : ''" @click="editPrice">
                            <el-icon class="mr-1">
                                <Edit />
                            </el-icon>
                            {{ tool.editPrice ? '完成' : '修改价格' }}
                        </el-button>
                        <el-button class="tool-btn" type="danger" plain @click="clearOrder">
                            <el-icon class="mr-1">
                                <Delete />
                            </el-icon>
                            清空商品
                        </el-button>
                        <!-- 移动端专用按钮 -->
                        <el-button v-if="mobile" class="tool-btn" plain type="success" @click="showOrder">
                            <el-icon class="mr-1">
                                <Check />
                            </el-icon>
                            收款
                        </el-button>
                        <el-button v-if="mobile" class="tool-btn" plain :type="tool.simple ? 'success' : ''"
                            @click="tool.simple = !tool.simple">
                            <el-icon class="mr-1">
                                <ZoomOut />
                            </el-icon>
                            精简
                        </el-button>
                    </div>
                </el-card>

                <!-- 会员信息卡片（仅桌面端显示） -->
                <el-card v-if="selectedMember && tool.vip && selectedMember.type !== 'INNER'" class="member-info-card">
                    <div class="member-header">
                        <el-avatar :size="32" class="bg-primary">
                            <el-icon>
                                <UserFilled />
                            </el-icon>
                        </el-avatar>
                        <div>
                            <div class="font-medium text-sm">{{ selectedMember.name }}</div>
                            <div class="text-xs el-text--secondary">{{ selectedMember.phone }}</div>
                        </div>
                    </div>
                    <el-divider class="my-2" />
                    <div class="member-coupon">
                        <div class="text-2xl font-bold text-primary">{{ selectedMember.coupon }}</div>
                        <div class="text-xs el-text--secondary">可用券</div>
                    </div>
                </el-card>
            </div>
        </div>

        <!-- 结算对话框 -->
        <SettleDialog v-model="dialogVisible" :order="order" :is-vip="tool.vip" :member="selectedMember"
            @confirm="settleAccounts" />
        <!-- 打印组件 -->
        <print-order ref="printOrder" />
    </PageWrapper>
</template>

<script setup>
import PageWrapper from "@/components/PageWrapper.vue"
import PrintOrder from "@/views/pos/printOrder.vue"
import SettleDialog from "./SettleDialog.vue"
import FestivalDecor from "@/components/FestivalDecor.vue"

import { computed, ref, onMounted, onUnmounted, watch } from "vue"
import { ElMessage, ElLoading } from "element-plus"
import {
    Refresh, User, Edit, Delete, Check, ZoomOut, Coin, Ticket, Money,
    Minus, Plus, UserFilled, Search
} from "@element-plus/icons-vue"
import NP from "number-precision"
import { isMobile } from "@/utils/index.js"
import { useSound } from "@/utils/sound.js"
import { useFestival } from "@/utils/festival.js"
import posApi from "@/api/pos/pos.js"

// ==================== 基础配置 ====================
const { play: playSound } = useSound()
const mobile = isMobile()
const festivalTheme = useFestival()

// ==================== 响应式数据 ====================
const goods = ref([])
const members = ref([])
const innerMember = ref()
const dialogVisible = ref(false)
const loading = ref(null)
const printOrder = ref()
const selectedMember = ref(null)
const order = ref([])
const tool = ref({
    vip: false,
    editPrice: false,
    simple: mobile
})

watch(dialogVisible, (val) => {
    if (!val) {
        festivalTheme.enabled.value = true
    }
})

// ==================== 计算属性 ====================
const total = computed(() => order.value.reduce((prev, next) => prev + (next.quantity | 0), 0))
const totalAmount = computed(() => order.value.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, next.salePrice)), 0))
const couponAmount = computed(() => order.value.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, tool.value.vip ? next.coupon : 0)), 0))
const payAmount = computed(() => order.value.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, next.goodsPrice)), 0))

// ==================== 商品选择 ====================
const goodsSelect = ref({
    barcode: null,
    queryGoods: (keyword, cb) => {
        if (goods.value.length < 16 && !keyword) {
            cb(goods.value)
        } else {
            const filterGoods = goods.value.filter(e => e.barcode.includes(keyword) || e.name.includes(keyword))
            cb(filterGoods.length > 15 ? [] : filterGoods)
        }
    },
    selectGoods: (item) => {
        const existing = order.value.find(e => e.goodsBarcode === item.barcode)
        if (existing) {
            existing.quantity += 1
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
        playSound('scan')
        goodsSelect.value.barcode = null
    },
    enterGoods: () => {
        const target = goods.value.find(e => e.barcode === goodsSelect.value.barcode)
        if (target) goodsSelect.value.selectGoods(target)
    }
})

// ==================== 会员选择 ====================
const memberSelect = ref({
    name: null,
    queryMember: (keyword, cb) => {
        if (members.value.length < 16 && !keyword) {
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
        playSound('member')
    }
})

// ==================== 订单操作 ====================
function changeQuantity(row, index) {
    if (row.quantity <= 0) {
        order.value.splice(index, 1)
        playSound('remove')
    }
}

function showOrder() {
    if (loading.value != null) return
    if (dialogVisible.value) {
        settleAccounts()
    } else {
        if (order.value.length > 0) {
            festivalTheme.enabled.value = false
            dialogVisible.value = true
        } else {
            ElMessage.warning('没有商品')
        }
    }
}

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
            playSound('success')
            dialogVisible.value = false
            const info = res.data
            if (info.vip && selectedMember.value) {
                selectedMember.value.coupon = NP.minus(selectedMember.value.coupon, info.couponAmount)
            }
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
            playSound('error')
            loading.value.close()
            loading.value = null
        })
}

function settleAccountsOk() {
    memberSelect.value.name = null
    selectedMember.value = null
    tool.value.vip = false
    tool.value.editPrice = false
    festivalTheme.enabled.value = true
    clearOrder()
    refresh()
}

// ==================== 工具操作 ====================
async function refresh() {
    goods.value = await posApi.listGoods().then(res => res.data)
    members.value = await posApi.listMember().then(res => res.data)
    innerMember.value = members.value.find(e => e.type === 'INNER')
}

function brushVip() {
    if (tool.value.vip) {
        selectedMember.value = null
        memberSelect.value.name = null
    } else {
        selectedMember.value = innerMember.value
        memberSelect.value.name = innerMember.value.name
    }
    tool.value.vip = !tool.value.vip
    order.value.forEach(o => {
        o.goodsPrice = tool.value.vip ? o.vipPrice : o.salePrice
    })
}

function editPrice() {
    tool.value.editPrice = !tool.value.editPrice
    order.value.forEach(o => o.oldGoodsPrice = o.goodsPrice)
}

function cancelEditPrice(row) {
    row.goodsPrice = row.oldGoodsPrice
}

function clearOrder() {
    if (order.value.length > 0) {
        playSound('clear')
    }
    order.value = []
}

function clearMember() {
    selectedMember.value = null
    tool.value.vip = false
    order.value.forEach(o => {
        o.goodsPrice = o.salePrice
    })
}

// ==================== 键盘快捷键 ====================
function handleKeydown(e) {
    if (e.code === 'Space') {
        e.preventDefault()
        showOrder()
    }
}

onMounted(() => {
    document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
    document.removeEventListener('keydown', handleKeydown)
})

// 初始化数据
refresh()
</script>

<style lang="less" scoped>
// ==================== 布局样式 ====================
.pos-container {
    display: flex;
    flex-direction: column-reverse;
    gap: 1rem;
    height: 100%;
    overflow: hidden;

    @media (min-width: 768px) {
        flex-direction: row;
        align-items: stretch;
        gap: 1rem;
    }
}

.pos-main {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    overflow: hidden;
}

.pos-sidebar {
    width: 100%;

    @media (min-width: 768px) {
        width: 160px;
        flex-shrink: 0;
    }
}

// ==================== 搜索区域 ====================
.search-card {
    flex-shrink: 0;
    
    :deep(.el-card__body) {
        padding: 0.75rem;
    }
}

.search-wrapper {
    display: flex;
    gap: 0.75rem;
    align-items: stretch;
}

.search-inputs {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.member-input-wrapper {
    position: relative;
}


.checkout-wrapper {
    display: none;

    @media (min-width: 768px) {
        display: flex;
        align-items: stretch;
    }
}

.checkout-btn {
    width: 140px;
    height: 100%;

    :deep(.el-button__text) {
        width: 100%;
        height: 100%;
    }
}

.checkout-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
}

.checkout-main {
    display: flex;
    align-items: center;
}

.checkout-shortcut {
    font-size: 0.75rem;
    padding: 0.125rem 0.5rem;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 0.25rem;
    font-family: monospace;
    color: white;
}

// ==================== 订单区域 ====================
.order-card {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    :deep(.el-card__header) {
        padding: 0.75rem 1rem;
        flex-shrink: 0;
    }

    :deep(.el-card__body) {
        padding: 0.5rem;
        flex: 1;
        overflow: auto;
    }
}

.order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 0.75rem;
    flex-wrap: wrap;
}

.order-header-left {
    display: flex;
    align-items: center;
    gap: 0.75rem;
}

.order-header-right {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    flex-wrap: wrap;
}

.summary-item {
    display: flex;
    align-items: center;
    gap: 0.25rem;
    font-size: 0.875rem;

    &.highlight {
        font-size: 1.125rem;
    }
}

.quantity-control {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.25rem;
}

// ==================== 工具栏 ====================
.tool-card {
    :deep(.el-card__body) {
        padding: 0.75rem;
    }
}

.tool-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 0.75rem;

    @media (min-width: 768px) {
        grid-template-columns: 1fr;
    }
}

.tool-btn {
    height: 3.5rem;
    margin: 0 !important;
}

// ==================== 会员信息卡片 ====================
.member-info-card {
    margin-top: 0.75rem;
    display: none;

    @media (min-width: 768px) {
        display: block;
    }

    :deep(.el-card__body) {
        padding: 0.75rem;
    }
}

.member-header {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    margin-bottom: 0.5rem;
}

.member-coupon {
    text-align: center;
}

// ==================== 通用样式 ====================
.suggestion-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0.25rem 0;
}

.suggestion-info {
    display: flex;
    flex-direction: column;
}

:deep(.el-autocomplete) {
    width: 100%;
}

:deep(.el-autocomplete input) {
    font-size: 16px;
}

:deep(.el-input__inner) {
    text-align: left;
}
</style>
