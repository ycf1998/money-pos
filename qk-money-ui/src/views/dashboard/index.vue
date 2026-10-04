<template>
    <PageWrapper>
        <el-tabs v-model="activeName" class="demo-tabs">
            <el-tab-pane v-for="(hc, index) in homeCount" :key="index" :label="hc.label" :name="hc.name">
                <div class="grid grid-cols-2 md:grid-cols-4 gap-2">
                    <el-card v-for="(item, index) in hc.data" :key="index" shadow="hover">
                        <template #header>
                            <span>{{ hc.label + item.title }}</span>
                        </template>
                        <div class="flex justify-between items-center">
                            <svg-icon :name="item.icon" class="w-8 h-8" />
                            <el-statistic :precision="item.precision" :value="item.count" />
                        </div>
                    </el-card>
                    <el-card shadow="hover">
                        <template #header>
                            <span>库存价值</span>
                        </template>
                        <div class="flex justify-between items-center">
                            <svg-icon name="home-inventoryValue" class="w-8 h-8" />
                            <el-statistic :precision="2" :value="inventoryValue" />
                        </div>
                    </el-card>
                </div>
            </el-tab-pane>
        </el-tabs>
    </PageWrapper>
</template>
<script setup>
import PageWrapper from "@/components/PageWrapper.vue";
import {onBeforeMount, ref} from "vue";
import homeApi from "@/api/dashboard/home.js";

const activeName = ref('today')
const homeCount = ref([])
const inventoryValue = ref(0)

onBeforeMount(async () => {
    const data = await homeApi.getHomeCount().then(res => res.data)
    inventoryValue.value = data.inventoryValue
    const flatGet = (data) => [{
        title: '订单数',
        icon: 'home-order',
        count: data.orderCount,
        precision: 0
    }, {
        title: '销售额',
        icon: 'home-sale',
        count: data.saleCount,
        precision: 2
    }, {
        title: '利润',
        icon: 'home-profit',
        count: data.profit,
        precision: 2
    }]
    homeCount.value.push({label: '今日', name: 'today', data: flatGet(data.today)})
    homeCount.value.push({label: '本月', name: 'month', data: flatGet(data.month)})
    homeCount.value.push({label: '本年', name: 'year', data: flatGet(data.year)})
    homeCount.value.push({label: '总', name: 'total', data: flatGet(data.total)})
})

</script>