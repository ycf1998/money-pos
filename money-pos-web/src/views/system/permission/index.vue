<template>
  <div class="app-container">
    <!-- 搜索 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <el-input v-model="query.condition" placeholder="模糊搜索" class="filter-item-200" @keyup.enter.native="crud.toQuery" />
      <el-select v-model="query.permissionType" clearable placeholder="类型" class="filter-item-200" @change="crud.toQuery">
        <el-option v-for="item in dict.permissionType" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <rr-operation />
    </div>
    <!-- CRUD操作 -->
    <crud-operation :permission="permission" :hidden-columns="['sort', 'createTime']" />
    <!-- 权限管理 -->
    <el-table v-loading="crud.loading" :data="crud.data" row-key="id" @select="crud.selectChange" @select-all="crud.selectAllChange" @selection-change="crud.selectionChangeHandler">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="permissionName" label="权限名称" />
      <el-table-column prop="icon" label="图标">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon ? scope.row.icon : ''" />
        </template>
      </el-table-column>
      <el-table-column prop="permission" label="权限标识" />
      <el-table-column prop="permissionType" label="资源类型">
        <template slot-scope="scope">
          {{ dict.label.permissionType[scope.row.permissionType] }}
        </template>
      </el-table-column>
      <el-table-column prop="componentPath" label="组件路径" />
      <el-table-column prop="hidden" label="隐藏">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.hidden" type="info">是</el-tag>
        </template>
      </el-table-column>
      <!-- <el-table-column prop="sort" label="排序" />
      <el-table-column prop="createTime" label="创建时间" /> -->
      <el-table-column align="center" label="操作" width="115" fixed="right">
        <template slot-scope="scope">
          <ud-operation :data="scope.row" :permission="permission" msg="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！" />
        </template>
      </el-table-column>
    </el-table>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="权限类型" prop="permissionType">
          <el-radio-group v-model="form.permissionType" size="mini" style="width: 178px" @change="changeType">
            <el-radio-button v-for="item in dict.permissionType" :key="item.id" :label="item.value">{{ item.label }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="form.permissionType !== 'BUTTON'" label="图标" prop="icon">
          <el-popover placement="bottom-start" width="400" trigger="click" @show="$refs['iconSelect'].reset()">
            <IconSelect ref="iconSelect" @selected="selected" />
            <el-input slot="reference" v-model="form.icon" style="width: 450px;" placeholder="点击选择图标" readonly>
              <svg-icon v-if="form.icon" slot="prefix" :icon-class="form.icon" class="el-input__icon" style="height: 32px;width: 16px;" />
              <i v-else slot="prefix" class="el-icon-search el-input__icon" />
            </el-input>
          </el-popover>
        </el-form-item>

        <el-form-item v-if="!type.BUTTON" label="标题" prop="permissionName">
          <el-input v-model="form.permissionName" :style="type.DIR ? 'width: 450px' : 'width: 178px'" placeholder="标题" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item v-if="type.BUTTON" label="按钮名称" prop="permissionName">
          <el-input v-model="form.permissionName" placeholder="按钮名称" style="width: 450px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item v-show="!type.DIR" label="权限标识" prop="permission">
          <el-input v-model="form.permission" :disabled="form.iframe === 'true'" placeholder="权限标识" :style="type.BUTTON ? 'width: 450px' : 'width: 178px'" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item v-if="!type.BUTTON" label="路由地址" prop="routerPath">
          <el-input v-model="form.routerPath" placeholder="路由地址" style="width: 450px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item v-show="form.iframe !== 'true' && type.MENU" label="组件名称" prop="componentName">
          <el-input v-model="form.componentName" style="width: 178px;" placeholder="匹配组件内Name字段" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item v-show="form.iframe !== 'true' && type.MENU" label="组件路径" prop="componentPath">
          <el-input v-model="form.componentPath" style="width: 178px;" placeholder="组件路径" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="上级类目" prop="parentId">
          <treeselect v-model="form.parentId" :options="permissions" style="width: 450px;" placeholder="选择上级类目" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model.number="form.sort" :min="0" :max="999" controls-position="right" style="width: 100px;" />
        </el-form-item>
        <el-form-item v-show="type.MENU" label="外链" prop="iframe">
          <el-radio-group v-model="form.iframe">
            <el-radio-button v-for="item in dict.yesOrNo" :key="item.id" :label="item.value">{{ item.label }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="!type.BUTTON" label="隐藏" prop="hidden">
          <el-radio-group v-model="form.hidden">
            <el-radio-button v-for="item in dict.yesOrNo" :key="item.id" :label="item.value">{{ item.label }}</el-radio-button>
          </el-radio-group>
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
import IconSelect from '@/components/IconSelect'
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

import permissionApi from '@/api/system/permission'

export default {
  name: 'Permission',
  components: { IconSelect, rrOperation, udOperation, crudOperation, Treeselect },
  cruds() {
    return CRUD({ title: '权限', url: '/permissions', crudMethod: { ...permissionApi }, isPage: false })
  },
  dicts: ['permissionType', 'yesOrNo'],
  mixins: [
    presenter(),
    header(),
    form({
      // 表单初始值
      id: null,
      permissionName: null,
      permissionType: 'DIR',
      icon: null,
      iframe: false,
      hidden: false,
      permission: null,
      routerPath: null,
      sort: 999,
      componentName: null,
      componentPath: null,
      parentId: null
    }),
    crud()
  ],
  data() {
    return {
      // 操作权限定义
      permission: {
        add: ['permission:add'],
        edit: ['permission:edit'],
        del: ['permission:del']
      },
      // 树形选择框数据
      permissions: [],
      // 当前选中类型
      type: {
        DIR: true,
        MENU: false,
        BUTTON: false
      },
      // 表单验证规则
      rules: [],
      dirRules: {
        icon: [{ required: true, message: '请选择图标', trigger: 'change' }],
        permissionName: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        routerPath: [{ required: true, message: '请填写路由地址', trigger: 'blur' }],
        parentId: [{ required: true, message: '请选择上级菜单', trigger: 'blur' }]
      },
      menuRules: {
        icon: [{ required: true, message: '请选择菜单图标', trigger: 'change' }],
        permissionName: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        routerPath: [{ required: true, message: '请填写路由地址', trigger: 'blur' }],
        parentId: [{ required: true, message: '请选择上级菜单', trigger: 'blur' }]
      },
      buttonRules: {
        permissionName: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        permission: [{ required: true, message: '请填写权限标识', trigger: 'blur' }],
        parentId: [{ required: true, message: '请选择上级菜单', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 查询后做的操作
    [CRUD.HOOK.afterRefresh](curd) {
      // 生成下拉树需要的结构
      const root = [{ id: 0, label: '顶级类目', children: null }]
      const tempFunc = (node, data) => {
        node.children = []
        data.forEach((element) => {
          const childrenNode = {
            id: element.id,
            label: element.permissionName
          }
          node.children.push(childrenNode)
          if (element.children) {
            tempFunc(childrenNode, element.children)
          }
        })
      }
      tempFunc(root[0], curd.data)
      this.permissions = root
    },
    // 新增前做的操作
    [CRUD.HOOK.afterToAdd](crud, form) {
      this.changeType(form.permissionType)
      this.rules = this.dirRules
      setTimeout(() => {
        this.$refs['form'].clearValidate()
      }, 50)
      form.parentId = 0
    },
    // 修改前做的操作
    [CRUD.HOOK.afterToEdit](crud, form) {
      this.changeType(form.permissionType)
    },
    // 切换类型
    changeType(type) {
      Object.keys(this.type).forEach((k) => {
        this.type[k] = k === type
      })
      switch (type) {
        case 'DIR':
          this.rules = this.dirRules
          break
        case 'MENU':
          this.rules = this.menuRules
          break
        case 'BUTTON':
          this.rules = this.buttonRules
          break
        default:
          this.rules = this.dirRules
          break
      }
      setTimeout(() => {
        this.$refs['form'].clearValidate()
      }, 50)
    },
    // 选中图标
    selected(icon) {
      this.form.icon = icon
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
