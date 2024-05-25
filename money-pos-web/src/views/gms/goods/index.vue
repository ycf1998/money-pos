<template>
    <PageWrapper>
        <div class="flex">
            <div class="mr-6 w-2/12 hidden lg:block">
                <GoodsCategory @node-click="selectGoodsCategory" />
            </div>
            <div class="grid gap-6 flex-1">
                <!-- 搜索栏 -->
                <MoneyRR :money-crud="moneyCrud">
                    <el-input v-model="moneyCrud.query.barcode" placeholder="条码" class="md:!w-48"
                              @keyup.enter.native="moneyCrud.doQuery" />
                    <el-input v-model="moneyCrud.query.name" placeholder="名称" class="md:!w-48"
                              @keyup.enter.native="moneyCrud.doQuery" />
                    <el-select v-model="moneyCrud.query.brandId" clearable class="w-full md:!w-48" placeholder="品牌"
                               @change="moneyCrud.doQuery">
                        <el-option v-for="item in brands" :key="item.value" :label="item.label" :value="item.value" />
                    </el-select>
                    <el-select v-model="moneyCrud.query.categoryId" clearable class="w-full md:!w-48 md:!hidden"
                               placeholder="分类"
                               @change="moneyCrud.doQuery">
                        <el-option v-for="item in categories" :key="item.value" :label="item.label"
                                   :value="item.value" />
                    </el-select>
                    <el-select v-model="moneyCrud.query.status" clearable placeholder="状态" class="md:!w-48"
                               @change="moneyCrud.doQuery">
                        <el-option v-for="item in dict.goodsStatus" :key="item.value" :label="item.desc"
                                   :value="item.value" />
                    </el-select>
                </MoneyRR>
                <!-- 操作行 -->
                <MoneyCUD :money-crud="moneyCrud" />
                <!-- 数据表格 -->
                <MoneyCrudTable :money-crud="moneyCrud">
                    <template #pic="{scope}">
                        <el-image
                            class="w-8 h-8"
                            preview-teleported
                            :src="$money.getOssUrl(scope.row.pic)"
                            :preview-src-list="[$money.getOssUrl(scope.row.pic)]"
                            fit="cover"
                        />
                    </template>
                    <template #brand="{scope}">
                        {{ brandsKv[scope.row.brandId] }}
                    </template>
                    <template #status="{scope}">
                        <el-tag :type="statusColor[scope.row.status] || 'primary'">
                            {{ dict.goodsStatusKv[scope.row.status] }}
                        </el-tag>
                    </template>
                    <template #stock="{scope}">
                        <el-input v-if="editCell.id === scope.row.id" v-model="scope.row.stock" style="width: 55px"
                                  @change="value => updateCell(value, scope.row)"
                                  @focusout="updateCell(null, scope.row)" />
                        <span v-else @click="startEditCell(scope.row.id, 'stock', scope.row.stock)">{{ scope.row.stock
                            }}</span>
                    </template>
                    <template #opt="{scope}">
                        <MoneyUD :money-crud="moneyCrud" :scope="scope" />
                    </template>
                </MoneyCrudTable>
                <!-- 表单 -->
                <MoneyForm :money-crud="moneyCrud" :rules="rules" :dialog-class="'!w-11/12 md:!w-5/12 !mt-12'">
                    <el-form-item label="图片" prop="pic">
                        <el-upload class="avatar-uploader" :auto-upload="false" :show-file-list="false" accept="image/*"
                                   :on-change="handlePicSuccess">
                            <img v-if="moneyCrud.form.pic" :src="$money.getOssUrl(moneyCrud.form.pic)" class="w-24"
                                 alt="pic">
                            <el-icon v-else class="avatar-uploader-icon !w-24 !h-24">
                                <Plus />
                            </el-icon>
                        </el-upload>
                    </el-form-item>
                    <div class="md:flex justify-between gap-2">
                        <el-form-item label="条码" prop="barcode" class="!w-full">
                            <el-input v-model.trim="moneyCrud.form.barcode" />
                        </el-form-item>
                        <el-form-item label="名称" prop="name" class="!w-full">
                            <el-input v-model.trim="moneyCrud.form.name" />
                        </el-form-item>
                    </div>
                    <div class="flex justify-between gap-2">
                        <el-form-item label="进价" prop="purchasePrice" class="!w-full">
                            <el-input v-model="moneyCrud.form.purchasePrice" />
                        </el-form-item>
                        <el-form-item label="售价" prop="salePrice" class="!w-full">
                            <el-input v-model="moneyCrud.form.salePrice" @input="computeVipPrice" />
                        </el-form-item>
                    </div>
                    <div class="flex justify-between gap-2">
                        <el-form-item label="用券" prop="coupon" class="!w-full">
                            <el-input v-model="moneyCrud.form.coupon" @input="computeVipPrice" />
                        </el-form-item>
                        <el-form-item label="会员价" prop="vipPrice" class="!w-full">
                            <el-input v-model="moneyCrud.form.vipPrice" />
                        </el-form-item>
                    </div>
                    <div class="flex justify-between gap-2">
                        <el-form-item label="状态" prop="status" class="!w-full">
                            <el-select v-model="moneyCrud.form.status" placeholder="请选择" class="w-full">
                                <el-option v-for="item in dict.goodsStatus" :key="item.value" :label="item.desc"
                                           :value="item.value" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="库存" prop="stock" class="!w-full">
                            <ComputeInput v-model="moneyCrud.form.stock" />
                        </el-form-item>
                    </div>
                    <div class="flex justify-between gap-2">
                        <el-form-item label="品牌" prop="brandId" class="!w-full">
                            <el-select v-model="moneyCrud.form.brandId" class="w-full" placeholder="请选择" clearable>
                                <el-option v-for="item in brands" :key="item.value" :label="item.label"
                                           :value="item.value" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="分类" prop="categoryId" class="!w-full">
                            <el-select v-model="moneyCrud.form.categoryId" class="w-full" placeholder="请选择"
                                       clearable>
                                <el-option v-for="item in categories" :key="item.value" :label="item.label"
                                           :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </div>
                    <div class="flex justify-between gap-2">
                        <el-form-item label="单位" prop="unit" class="!w-full">
                            <el-input v-model.trim="moneyCrud.form.unit" />
                        </el-form-item>
                        <el-form-item label="规格" prop="size" class="!w-full">
                            <el-input v-model.trim="moneyCrud.form.size" />
                        </el-form-item>
                    </div>
                    <el-form-item label="描述">
                        <el-input v-model.trim="moneyCrud.form.description" type="textarea" maxlength="250"
                                  show-word-limit />
                    </el-form-item>
                </MoneyForm>
            </div>
        </div>
    </PageWrapper>
</template>

<script setup>
import PageWrapper from "@/components/PageWrapper.vue";
import MoneyCrud from '@/components/crud/MoneyCrud.js'
import MoneyCrudTable from "@/components/crud/MoneyCrudTable.vue";
import MoneyRR from "@/components/crud/MoneyRR.vue";
import MoneyCUD from "@/components/crud/MoneyCUD.vue";
import MoneyUD from "@/components/crud/MoneyUD.vue";
import MoneyForm from "@/components/crud/MoneyForm.vue";
import ComputeInput from "@/components/ComputeInput.vue";
import GoodsCategory from "@/views/gms/goods/GoodsCategory.vue";

import {ref} from "vue";
import {useUserStore} from "@/store/index.js";
import NP from 'number-precision'
import brandApi from "@/api/gms/brand.js"
import goodsApi from "@/api/gms/goods.js"
import dictApi from "@/api/system/dict.js";
import goodsCategoryApi from "@/api/gms/goodsCategory.js";

const userStore = useUserStore()
const columns = [
    {prop: 'pic', label: '图片', show: false},
    {prop: 'barcode', label: '条码', width: 140},
    {prop: 'brand', label: '品牌', show: false},
    {prop: 'name', label: '名称', width: 140},
    {prop: 'status', label: '状态'},
    {prop: 'salePrice', label: '售价'},
    {prop: 'vipPrice', label: '会员价'},
    {prop: 'coupon', label: '用券'},
    {prop: 'purchasePrice', label: '进价'},
    {prop: 'stock', label: '库存'},
    {prop: 'sales', label: '销量', sortable: 'custom'},
    {prop: 'unit', label: '单位', show: false},
    {prop: 'size', label: '规格', show: false},
    {prop: 'createTime', label: '创建时间', width: 180, show: false},
    {prop: 'updateTime', label: '更新时间', width: 180, show: false},
    {prop: 'description', label: '描述', show: false},
    {
        prop: 'opt',
        label: '操作',
        width: 120,
        align: 'center',
        fixed: 'right',
        showOverflowTooltip: false,
        isMoneyUD: true
    },
]
const rules = {
    barcode: [
        {required: true, message: '请输入条码'}
    ],
    name: [
        {required: true, message: '请输入名称'}
    ],
    purchasePrice: [
        {required: true, message: '请输入进价'},
        {pattern: /^[1-9]\d*(\.\d+)?$/, message: '仅支持正数'}
    ],
    salePrice: [
        {required: true, message: '请输入售价'},
        {pattern: /^[1-9]\d*(\.\d+)?$/, message: '仅支持正数'}
    ],
    coupon: [
        {required: true, message: '请输入用券'},
        {pattern: /^(0|[1-9]\d*)(\.\d+)?$/, message: '仅支持0和正数'}
    ],
    vipPrice: [
        {required: true, message: '请输入会员价'},
        {pattern: /^[1-9]\d*(\.\d+)?$/, message: '仅支持正数'}
    ],
    stock: [
        {required: true, message: '请输入库存'},
        {pattern: /^(0|[1-9]\d*)$/, message: '仅支持0和正整数'}
    ],
    status: [
        {required: true, message: '请选择状态'}
    ]
}
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: goodsApi,
    optShow: {
        checkbox: userStore.hasPermission(['gmsGoods:edit', 'gmsGoods:del']),
        add: userStore.hasPermission('gmsGoods:add'),
        edit: userStore.hasPermission('gmsGoods:edit'),
        del: userStore.hasPermission('gmsGoods:del')
    },
    defaultForm: {
        status: 'SALE',
        stock: 0
    }
}))
const dict = ref({})
const brands = ref([])
const brandsKv = ref({})
const categories = ref([])
const statusColor = {
    'SOLD_OUT': 'warning',
    'UN_SHELVE': 'info',
}
moneyCrud.value.init(moneyCrud, async () => {
    brands.value = await brandApi.getSelect().then(res => res.data)
    brands.value.forEach(e => {
        brandsKv.value[e.value] = e.label
    })
    categories.value = await goodsCategoryApi.getSelect().then(res => res.data)
    dict.value = await dictApi.loadDict(["goodsStatus"])
})

function selectGoodsCategory(data) {
    moneyCrud.value.query.categoryId = data.id !== 0 ? data.id : null
    moneyCrud.value.doQuery()
}

function handlePicSuccess(file) {
    moneyCrud.value.form.pic = URL.createObjectURL(file.raw)
    moneyCrud.value.form.picFile = file.raw
}

function computeVipPrice() {
    // VIP 价格 = 售价 - 用券
    moneyCrud.value.form.vipPrice = NP.minus(moneyCrud.value.form.salePrice, moneyCrud.value.form.coupon)
}

// 编辑单元格
const editCell = ref({})

function startEditCell(id, field, origin) {
    editCell.value.id = id
    editCell.value.field = field
    editCell.value.origin = origin
}

function updateCell(value, row) {
    if (value === editCell.value.origin || !/^\d+$/.test(value)) {
        row[editCell.value.field] = editCell.value.origin
        editCell.value = {}
        return
    }
    goodsApi.edit({
        id: row.id,
        [editCell.value.field]: value
    }).then(() => moneyCrud.value.messageOk())
        .catch(() => row[editCell.value.field] = editCell.value.origin)
    editCell.value = {}
}
</script>