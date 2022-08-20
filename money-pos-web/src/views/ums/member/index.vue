<template>
  <div class="app-container">
    <!-- 搜索 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <el-input v-model="query.code" placeholder="会员号" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-input v-model="query.name" placeholder="会员名称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-input v-model="query.phone" placeholder="手机号码" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-select
        v-model="query.type"
        clearable
        placeholder="会员类型"
        class="filter-item"
        @change="crud.toQuery"
      >
        <el-option
          v-for="item in dict.memberType"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <rr-operation />
    </div>
    <!-- CRUD操作 -->
    <crud-operation :permission="permission" :hidden-columns="['code']" />
    <!-- 会员列表 -->
    <el-table
      ref="table"
      v-loading="crud.loading"
      :data="crud.data"
      style="width: 100%;"
      @selection-change="crud.selectionChangeHandler"
      @sort-change="crud.sortChangeHandler"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column :show-overflow-tooltip="true" prop="code" label="会员号" />
      <el-table-column :show-overflow-tooltip="true" prop="name" label="会员名称" />
      <el-table-column :show-overflow-tooltip="true" prop="type" label="会员类型">
        <template slot-scope="scope">
          {{ dict.label.memberType[scope.row.type] }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="phone" label="手机号码" />
      <el-table-column :show-overflow-tooltip="true" prop="coupon" label="抵用券" />
      <el-table-column :show-overflow-tooltip="true" sortable prop="consumeAmount" label="总消费金额" />
      <el-table-column :show-overflow-tooltip="true" prop="consumeCoupon" label="消费抵用券" />
      <el-table-column :show-overflow-tooltip="true" sortable prop="consumeTimes" label="消费次数" />
      <el-table-column :show-overflow-tooltip="true" sortable prop="cancelTimes" label="取消订单次数" />
      <el-table-column width="150" prop="address" label="地址">
        <template slot-scope="scope">
          {{ scope.row.province + scope.row.city + scope.row.district }}
          <el-tooltip v-if="scope.row.address" class="item" effect="light" :content="scope.row.address" placement="top-start">
            <i class="el-icon-notebook-1" />
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="remark" label="备注" />
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
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="460px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="100px">
        <el-form-item label="会员名称" prop="name">
          <el-input v-model="form.name" style="width: 200px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="会员类型" prop="type">
          <el-select
            v-model="form.type"
            style="width: 200px;"
            placeholder="会员类型"
          >
            <el-option
              v-for="item in dict.memberType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" style="width: 200px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="抵用券" prop="coupon">
          <Compute-Input v-model="form.coupon" style="width: 200px;" />
        </el-form-item>
        <el-form-item label="地址" prop="">
          <Address-Linkage :province="form.province" :city="form.city" :district="form.district" @select-change="addressSelectChnage" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input
            v-model.trim="form.address"
            type="textarea"
            style="width: 200px;"
            maxlength="250"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model.trim="form.remark"
            type="textarea"
            style="width: 200px;"
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
import crudMember from '@/api/ums/member'
import ComputeInput from '@/components/ComputeInput'
import AddressLinkage from '@/components/AddressLinkage'
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import Pagination from '@/components/Crud/Pagination.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'
import { validatePhone, validNonnegative } from '@/utils/validate'

export default {
  name: 'Member',
  components: { Pagination, rrOperation, udOperation, crudOperation, ComputeInput, AddressLinkage },
  cruds() {
    return CRUD({ title: '会员', url: '/members', crudMethod: { ...crudMember }})
  },
  mixins: [presenter(), header(), form({
    // 表单初始值
    id: null,
    name: null,
    type: 'MEMBER',
    phone: null,
    province: null,
    city: null,
    district: null,
    address: null,
    coupon: 0.00,
    remark: null
  }), crud()],
  dicts: ['memberType'],
  data() {
    return {
      // 操作权限定义
      permission: {
        add: ['member:add'],
        edit: ['member:edit'],
        del: ['member:del']
      },
      phoneUrl: '',
      // 表单验证规则
      rules: {
        name: [
          { required: true, message: '请输入会员名称', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择会员类型', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { trigger: 'blur', validator: validatePhone }
        ],
        coupon: [
          { required: true, message: '请输入抵用券', trigger: 'blur' },
          { trigger: 'blur', validator: validNonnegative }
        ]
      }
    }
  },
  methods: {
    // 省市联动选择
    addressSelectChnage(province, city, district) {
      this.form.province = province
      this.form.city = city
      this.form.district = district
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
