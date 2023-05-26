<template>
  <div class="app-container">
    <!-- 搜索 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <el-input v-model="query.tenantCode" placeholder="编码" class="filter-item-200" @keyup.enter.native="crud.toQuery" />
      <el-input v-model="query.tenantName" placeholder="名称" class="filter-item-200" @keyup.enter.native="crud.toQuery" />
      <rr-operation />
    </div>
    <!-- CRUD操作 -->
    <crud-operation :permission="permission" />
    <!-- 租户管理 -->
    <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
      <el-table-column type="selection" width="55" :selectable="row => row.id !== 0" />
      <el-table-column :show-overflow-tooltip="true" prop="tenantCode" label="租户编码" />
      <el-table-column :show-overflow-tooltip="true" prop="tenantName" label="租户名称" />
      <el-table-column :show-overflow-tooltip="true" prop="logo" label="logo">
        <template slot-scope="scope">
          <el-image style="width: 25px; height: 25px" :src="loadLogo(scope.row.logo)" :preview-src-list="[loadLogo(scope.row.logo)]" />
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="domain" label="域名" />
      <el-table-column :show-overflow-tooltip="true" prop="tenantDesc" label="租户描述" />
      <el-table-column label="操作" width="115" align="center" fixed="right">
        <template slot-scope="scope">
          <ud-operation :data="scope.row" :permission="permission" :disabled-del="scope.row.id === 0" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination />
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="400px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="100px">
        <el-form-item label="租户编码" prop="tenantCode">
          <el-input v-model="form.tenantCode" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="form.tenantName" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="域名" prop="domain">
          <el-input v-model="form.domain" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="logo" prop="logo">
          <el-upload class="avatar-uploader" action="" :auto-upload="false" :on-change="handleLogoSuccess" :show-file-list="false" accept="image/*">
            <img v-if="form.logo" :src="loadLogo(form.logo)" style="width: 50px; height: 50px">
            <i v-else class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <el-form-item label="租户描述">
          <el-input v-model.trim="form.tenantDesc" type="textarea" maxlength="250" show-word-limit />
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
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import Pagination from '@/components/Crud/Pagination.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'
import oss from '@/utils/oss'

import tenantApi from '@/api/system/tenant'

export default {
  name: 'Tenant',
  components: { Pagination, rrOperation, udOperation, crudOperation },
  cruds() {
    return CRUD({ title: '租户', url: '/tenants', crudMethod: { ...tenantApi } })
  },
  mixins: [
    presenter(),
    header(),
    form({
      // 表单初始值
      id: null,
      tenantCode: null,
      tenantName: null,
      tenantDesc: null,
      domain: null,
      logo: null,
      logoFile: null
    }),
    crud()
  ],
  data() {
    return {
      // 操作权限定义
      permission: {
        add: ['tenant:add'],
        edit: ['tenant:edit'],
        del: ['tenant:del']
      },
      // 表单验证规则
      rules: {
        tenantCode: [{ required: true, message: '请输入租户编码', trigger: 'blur' }],
        tenantName: [{ required: true, message: '请输入租户名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    // 修改新增成功提示信息
    this.crud.msg.add = '新增成功, 租户默认管理员账号: admin, 密码: admin'
  },
  methods: {
    // 加载logo
    loadLogo(url) {
      return oss.loadImage(url, oss.TYPE.LOCAL)
    },
    // 选择logo预览
    handleLogoSuccess(file) {
      this.form.logo = URL.createObjectURL(file.raw)
      this.form.logoFile = file.raw
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
