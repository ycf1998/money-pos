<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="0" :sm="6" :md="4">
        <GoodsCategoryTree @node-click="selectCategory" @node-update="loadCategories" />
      </el-col>
      <el-col :xs="24" :sm="18" :md="20">
        <!-- 搜索 -->
        <div v-if="crud.props.searchToggle" class="filter-container">
          <el-input v-model="query.barcode" placeholder="条码" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
          <el-input v-model="query.name" placeholder="名称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
          <el-select
            v-model="query.brandId"
            clearable
            class="filter-item"
            placeholder="品牌"
            @change="crud.toQuery"
          >
            <el-option
              v-for="item in brands.data"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <el-select
            v-model="query.status"
            clearable
            placeholder="状态"
            class="filter-item"
            style="width: 90px"
            @change="crud.toQuery"
          >
            <el-option
              v-for="item in dict.goodsStatus"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <rr-operation />
        </div>
        <!-- CRUD操作 -->
        <crud-operation :permission="permission" :hidden-columns="['brand', 'label', 'pic', 'unit', 'size', 'description', 'createTime', 'updateTime']" />
        <!-- 商品列表 -->
        <el-table
          ref="table"
          v-loading="crud.loading"
          :data="crud.data"
          style="width: 100%;"
          @selection-change="crud.selectionChangeHandler"
          @sort-change="crud.sortChangeHandler"
        >
          <el-table-column align="center" type="selection" width="55" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="label" label="标签" />
          <el-table-column align="center" prop="pic" label="图片">
            <template slot-scope="scope">
              <el-image
                style="width: 100px; height: 100px"
                :src="loadPic(scope.row.pic)"
              />
            </template>
          </el-table-column>
          <el-table-column align="center" prop="barcode" label="条码" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="brand" label="品牌">
            <template slot-scope="scope">
              {{ brands.label[scope.row.brandId] }}
            </template>
          </el-table-column>
          <el-table-column align="center" prop="name" label="名称">
            <template slot-scope="scope">
              <el-badge :value="dict.label.goodsStatus[scope.row.status]" class="item" :type="badgeType(scope.row.status)">
                {{ scope.row.name }}
              </el-badge>
            </template>
          </el-table-column>
          <el-table-column align="center" :show-overflow-tooltip="true" prop="salePrice" label="售价" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="vipPrice" label="会员价" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="coupon" label="用券" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="purchasePrice" label="进价" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="stock" label="库存" />
          <el-table-column align="center" :show-overflow-tooltip="true" sortable prop="sales" label="销量" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="unit" label="单位" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="size" label="规格" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="createTime" label="创建时间" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="updateTime" label="更新时间" />
          <el-table-column align="center" :show-overflow-tooltip="true" prop="description" label="描述" />
          <el-table-column
            label="操作"
            width="115"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <ud-operation :data="scope.row" :permission="permission" />
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <pagination />
      </el-col>
    </el-row>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="620px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="图片" prop="pic">
          <el-upload
            class="avatar-uploader"
            action=""
            :auto-upload="false"
            :on-change="handlePicSuccess"
            :show-file-list="false"
            accept="image/*"
          >
            <img v-if="form.pic" :src="loadPic(form.pic)" style="width: 100px; height: 100px">
            <i v-else class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <br>
        <el-form-item label="条码" prop="barcode">
          <el-input v-model="form.barcode" style="width: 450px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" style="width: 450px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="进价" prop="purchasePrice">
          <el-input v-model="form.purchasePrice" style="width: 178px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="售价" prop="salePrice">
          <el-input v-model="form.salePrice" style="width: 178px;" @input="computeVipPrice" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="用券" prop="coupon">
          <el-input v-model="form.coupon" style="width: 178px;" @input="computeVipPrice" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="会员价" prop="vipPrice">
          <el-input v-model="form.vipPrice" style="width: 178px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="form.status"
            style="width: 178px;"
            placeholder="请选择"
          >
            <el-option
              v-for="item in dict.goodsStatus"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <Compute-Input v-model="form.stock" style="width: 178px;" />
        </el-form-item>
        <el-form-item label="品牌" prop="brandId">
          <el-select
            v-model="form.brandId"
            style="width: 178px;"
            placeholder="请选择"
          >
            <el-option
              v-for="item in brands.data"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select
            v-model="form.categoryId"
            style="width: 178px;"
            placeholder="请选择"
          >
            <el-option
              v-for="item in categories.data"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" style="width: 178px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="规格" prop="size">
          <el-input v-model="form.size" style="width: 178px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model.trim="form.description"
            style="width: 450px;"
            type="textarea"
            maxlength="250"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import crudGoods from '@/api/gms/goods'
import { selectBrand } from '@/api/gms/brand'
import { selectCategory } from '@/api/gms/goodsCategory'

import GoodsCategoryTree from './goodsCategoryTree.vue'
import ComputeInput from '@/components/ComputeInput'
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import Pagination from '@/components/Crud/Pagination.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'

import oss from '@/utils/oss'
import { validNonnegative } from '@/utils/validate'
import calculator from '@/utils/calculator'

export default {
  name: 'Goods',
  components: { Pagination, rrOperation, udOperation, crudOperation, ComputeInput, GoodsCategoryTree },
  cruds() {
    return CRUD({ title: '商品', url: '/goods', crudMethod: { ...crudGoods }})
  },
  mixins: [presenter(), header(), form({
    // 表单初始值
    id: null,
    pic: null,
    picFile: null,
    status: 'SALE',
    brandId: 0,
    categoryId: 0,
    barcode: null,
    name: null,
    purchasePrice: null,
    salePrice: null,
    coupon: null,
    vipPrice: null,
    stock: 0,
    unit: null,
    size: null,
    description: null
  }), crud()],
  dicts: ['goodsStatus'],
  data() {
    return {
      // 操作权限定义
      permission: {
        add: ['goods:add'],
        edit: ['goods:edit'],
        del: ['goods:del']
      },
      brands: {
        label: { 0: '无' },
        data: [{ label: '无', value: 0 }]
      },
      categories: {
        label: { 0: '全部分类' },
        data: [{ label: '全部分类', value: 0 }]
      },
      // 表单验证规则
      rules: {
        barcode: [
          { required: true, message: '请输入条码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        purchasePrice: [
          { required: true, message: '请输入进价', trigger: 'blur' },
          { trigger: 'blur', validator: validNonnegative }
        ],
        salePrice: [
          { required: true, message: '请输入售价', trigger: 'blur' },
          { trigger: 'blur', validator: validNonnegative }
        ],
        coupon: [
          { required: true, message: '请输入用券', trigger: 'blur' },
          { trigger: 'blur', validator: validNonnegative }
        ],
        vipPrice: [
          { required: true, message: '请输入会员价', trigger: 'blur' },
          { trigger: 'blur', validator: validNonnegative }
        ],
        stock: [
          { required: true, message: '请输入库存', trigger: 'blur' },
          { trigger: 'blur', validator: validNonnegative }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadBrands()
    this.loadCategories()
  },
  methods: {
    // 获取品牌下拉框
    loadBrands() {
      selectBrand().then(res => {
        this.brands.data.push(...res.data)
        res.data.forEach(e => {
          this.brands.label[e.value] = e.label
        })
      })
    },
    // 获取分类下拉框
    loadCategories() {
      selectCategory().then(res => {
        this.categories.data.push(...res.data)
        res.data.forEach(e => {
          this.categories.label[e.value] = e.label
        })
      })
    },
    // 选中分类
    selectCategory(val) {
      this.query.categoryId = val
      this.crud.toQuery()
    },
    // 计算会员价
    computeVipPrice() {
      if (this.form.salePrice && this.form.coupon) {
        this.form.vipPrice = calculator.Sub(this.form.salePrice, this.form.coupon)
      }
    },
    // 标记type
    badgeType(status) {
      switch (status) {
        case 'SOLD_OUT': return 'warning'
        case 'UN_SHELVE': return 'info'
        default: return 'primary'
      }
    },
    // 加载pic
    loadPic(url) {
      return oss.loadImage(url, oss.TYPE.LOCAL)
    },
    // 选择pic预览
    handlePicSuccess(file) {
      this.form.pic = URL.createObjectURL(file.raw)
      this.form.picFile = file.raw
    },
    // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.item {
  margin-top: 10px;
  .el-badge__content.is-fixed {
    right: 0 !important;
  }
}
</style>
