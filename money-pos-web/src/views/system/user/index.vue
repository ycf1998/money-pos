<template>
  <div class="app-container">
    <!-- 搜索 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <el-input v-model="query.name" placeholder="用户名/昵称" class="filter-item-200" @keyup.enter.native="crud.toQuery" />
      <el-input v-model.number="query.phone" placeholder="手机号" class="filter-item-200" @keyup.enter.native="crud.toQuery" />
      <el-select v-model="query.enabled" clearable placeholder="状态" class="filter-item-200" @change="crud.toQuery">
        <el-option v-for="item in dict.switch" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <rr-operation />
    </div>
    <!-- CRUD操作 -->
    <crud-operation :permission="permission" :hidden-columns="['createTime', 'updateTime', 'remark']" />
    <!-- 用户管理 -->
    <el-table ref="table" v-loading="crud.loading" :data="crud.data" @selection-change="crud.selectionChangeHandler" @sort-change="crud.sortChangeHandler">
      <el-table-column :selectable="row => row.id !== user.id" type="selection" width="55" />
      <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" />
      <el-table-column :show-overflow-tooltip="true" prop="nickname" label="昵称" />
      <el-table-column :show-overflow-tooltip="true" prop="phone" label="手机号" />
      <el-table-column :show-overflow-tooltip="true" prop="email" label="邮箱" />
      <el-table-column label="最高角色" prop="roles">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.roles[0].roleName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="enabled">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enabled" :disabled="user.id === scope.row.id" active-color="#409EFF" inactive-color="#F56C6C" @change="changeEnabled(scope.row, scope.row.enabled)" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column prop="updateTime" label="修改时间" />
      <el-table-column sortable prop="lastTime" label="最近登录时间" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column align="center" label="操作" width="115" fixed="right">
        <template slot-scope="scope">
          <ud-operation :data="scope.row" :permission="permission" :disabled-edit="scope.row.id === user.id" :disabled-del="scope.row.id === user.id" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination />
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="380px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="crud.status.isEdit" style="width: 220px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" style="width: 220px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model.number="form.phone" style="width: 220px;" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" style="width: 220px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.enabled">
            <el-radio v-for="item in dict.switch" :key="item.id" :label="item.value">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色" prop="roles">
          <el-select v-model="selectedRoles" style="width: 220px" multiple placeholder="请选择">
            <el-option v-for="item in roles" :key="item.roleName" :label="item.roleName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model.trim="form.remark" style="width: 220px" type="textarea" maxlength="250" show-word-limit />
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
import { mapGetters } from 'vuex'
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import Pagination from '@/components/Crud/Pagination.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'

import userApi from '@/api/system/user'
import { getAllRole } from '@/api/system/role'
import { validatePhone } from '@/utils/validate'

export default {
  name: 'User',
  components: { Pagination, rrOperation, udOperation, crudOperation },
  cruds() {
    return CRUD({ title: '用户', url: '/users', crudMethod: { ...userApi } })
  },
  mixins: [
    presenter(),
    header(),
    form({
      // 表单初始值
      id: null,
      username: null,
      nickname: null,
      email: null,
      phone: null,
      remark: null,
      enabled: 'true',
      roles: []
    }),
    crud()
  ],
  dicts: ['switch'],
  data() {
    return {
      // 操作权限定义
      permission: {
        add: ['user:add'],
        edit: ['user:edit'],
        del: ['user:del']
      },
      // 选中的角色
      selectedRoles: [],
      // 所有角色
      roles: [],
      // 表单验证规则
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { trigger: 'blur', validator: validatePhone }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['user'])
  },
  created() {
    // 修改新增成功提示信息
    this.crud.msg.add = '新增成功, 默认密码: 123456'
  },
  methods: {
    // 新增与编辑前做的操作
    async [CRUD.HOOK.afterToCU]() {
      this.roles = await getAllRole().then((res) => res.data)
    },
    // 新增弹窗前操作
    [CRUD.HOOK.beforeToAdd]() {
      this.selectedRoles = []
    },
    // 修改弹窗前操作
    [CRUD.HOOK.beforeToEdit](crud, row) {
      this.form.enabled = row.enabled.toString()
      // 回显角色
      this.selectedRoles = []
      const _this = this
      row.roles.forEach((role) => {
        _this.selectedRoles.push(role.id)
      })
    },
    // 新增修改前操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (this.selectedRoles.length === 0) {
        this.$message({
          message: '角色不能为空',
          type: 'warning'
        })
        return false
      }
      crud.form.roles = this.selectedRoles
      return true
    },
    // 改变用户状态
    changeEnabled(data, val) {
      let confirm = `此操作将${val ? '启用' : '禁用'}用户 【${data.username}】，是否继续？`
      this.$confirm(confirm, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          userApi
            .edit({
              id: data.id,
              enabled: val
            })
            .then(() => {
              this.crud.submitSuccessNotify()
            })
            .catch(() => {
              data.enabled = !data.enabled
            })
        })
        .catch(() => {
          data.enabled = !data.enabled
        })
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
