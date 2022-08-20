<template>
  <div class="app-container">
    <!-- 搜索 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <el-input v-model="query.roleCode" placeholder="角色编码" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-input v-model="query.name" placeholder="角色名称/描述" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <el-select
        v-model="query.enabled"
        clearable
        placeholder="状态"
        class="filter-item"
        style="width: 90px"
        @change="crud.toQuery"
      >
        <el-option
          v-for="item in dict.switch"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <rr-operation />
    </div>
    <!-- CRUD操作 -->
    <crud-operation :permission="permission" />
    <el-row :gutter="20">
      <el-col :span="16">
        <!-- 角色管理 -->
        <el-table
          ref="table"
          v-loading="crud.loading"
          highlight-current-row
          :data="crud.data"
          style="width: 100%;"
          @selection-change="crud.selectionChangeHandler"
          @current-change="handleCurrentChange"
        >
          <el-table-column :selectable="checkboxT" type="selection" width="55" />
          <el-table-column prop="roleCode" label="角色编码" />
          <el-table-column prop="roleName" label="角色名称" />
          <el-table-column prop="level" label="角色级别" />
          <el-table-column :show-overflow-tooltip="true" prop="description" label="描述" />
          <el-table-column label="状态" align="center" prop="enabled">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.enabled"
                active-color="#409EFF"
                inactive-color="#F56C6C"
                @change="changeEnabled(scope.row, scope.row.enabled)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="count" label="角色人数" />
          <el-table-column
            label="操作"
            width="115"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <ud-operation
                :data="scope.row"
                :permission="permission"
                :disabled-edit="scope.row.level < user.level"
                :disabled-del="scope.row.level < user.level"
              />
            </template>
          </el-table-column>
        </el-table>
        <pagination />
      </el-col>
      <el-col :span="8">
        <!-- 菜单授权 -->
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <el-tooltip class="item" effect="dark" content="选择指定角色分配菜单" placement="top">
              <span class="role-span">权限分配</span>
            </el-tooltip>
            <el-button
              v-permission="['role:edit']"
              :disabled="!showButton"
              :loading="permissionLoad"
              icon="el-icon-check"
              size="mini"
              style="float: right; padding: 6px 9px"
              type="primary"
              @click="saveConfig"
            >保存</el-button>
          </div>
          <el-tree
            ref="permission"
            lazy
            :data="permissions"
            :default-checked-keys="selectedPermissions"
            :load="getPermissions"
            :props="{ children: 'children', label: 'permissionName', isLeaf: 'leaf' }"
            check-strictly
            show-checkbox
            node-key="id"
            @check="permissionChange"
          />
        </el-card>
      </el-col>
    </el-row>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="350px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" :disabled="crud.status.isEdit" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="角色级别" prop="level">
          <el-input-number v-model.number="form.level" :min="user.level" :max="99" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.enabled">
            <el-radio
              v-for="item in dict.switch"
              :key="item.id"
              :label="item.value"
            >{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input
            v-model.trim="form.description"
            style="width: 200px"
            type="textarea"
            maxlength="50"
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
import { mapGetters } from 'vuex'
import crudRole from '@/api/system/role'
import { getPermissionsLazy, getAllSubIds } from '@/api/system/permission'
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import Pagination from '@/components/Crud/Pagination.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'

export default {
  name: 'Role',
  components: { Pagination, rrOperation, udOperation, crudOperation },
  cruds() {
    return CRUD({ title: '角色', url: '/roles', crudMethod: { ...crudRole }})
  },
  dicts: ['switch'],
  mixins: [presenter(), header(), form({
    // 表单初始值
    id: null,
    roleCode: null,
    roleName: null,
    level: 1,
    description: null,
    enabled: true
  }), crud()],
  data() {
    return {
      // 操作权限定义
      permission: {
        add: ['role:add'],
        edit: ['role:edit'],
        del: ['role:del']
      },
      // 表单验证规则
      rules: {
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      },
      showButton: false,
      permissionLoad: false,
      // 当前选中的角色id
      selectedRole: null,
      // 所有权限
      permissions: [],
      // 选中的权限
      selectedPermissions: []
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  methods: {
    // 新增弹窗前操作
    [CRUD.HOOK.beforeToAdd](crud, form) {
      form.enabled = 'true'
    },
    // 修改弹窗前操作
    [CRUD.HOOK.beforeToEdit](crud, form) {
      form.enabled = form.enabled.toString()
    },
    // 修改角色可用状态
    changeEnabled(data, val) {
      this.$confirm('此操作将 "' + this.dict.label.switch[val] + '" ' + data.roleName + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        crudRole.changeStatus(data.id, val).then(res => {
          this.crud.notify(this.dict.label.switch[val] + '成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(() => {
          data.enabled = !data.enabled
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    },
    // 选中角色
    handleCurrentChange(val) {
      if (val) {
        const _this = this
        this.selectedRole = val.id
        // 清空原本选中
        this.$refs.permission.setCheckedKeys([])
        // 初始化默认选中的key
        this.selectedPermissions = []
        val.permissions.forEach(function(data) {
          _this.selectedPermissions.push(data.id)
        })
        this.showButton = true
      }
    },
    // 加载下级权限
    getPermissions(node, resolve) {
      setTimeout(() => {
        getPermissionsLazy(node.data.id ? node.data.id : 0).then(res => {
          // el-tree 根据leaf属性显示展开按钮
          res.data.forEach(e => { e.leaf = e.subCount === 0 })
          resolve(res.data)
        })
      }, 100)
    },
    // 选中权限
    permissionChange(permission) {
      getAllSubIds(permission.id).then(res => {
        const subIds = res.data
        // 如果已有下级被选中，有漏选补选，无漏选反向
        if (subIds.every(e => this.selectedPermissions.includes(e))) {
          this.selectedPermissions = this.selectedPermissions.filter(e => !subIds.includes(e))
        } else {
        // 取已选中和本次选中（包含子节点）并集
          this.selectedPermissions = [...new Set([...this.selectedPermissions, ...subIds])]
        }
        this.$refs.permission.setCheckedKeys(this.selectedPermissions)
      })
    },
    // 保存权限配置
    saveConfig() {
      this.permissionLoad = true
      crudRole.configurePermissions(this.selectedRole, this.selectedPermissions).then(() => {
        this.crud.notify('保存成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        this.permissionLoad = false
        this.crud.refresh()
      }).catch(err => {
        this.permissionLoad = false
        console.log(err.response.data.message)
      })
    },
    // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false
      }
    },
    // 复选框是否可选
    checkboxT(row, rowIndex) {
      return !(row.level < this.user.level)
    }
  }
}
</script>
