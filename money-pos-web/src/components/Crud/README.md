# Money CRUD 组件

一套基于 Vue 3 + Element Plus 的 CRUD（增删改查）快速开发组件，封装了表格、表单、搜索栏等常用功能，大幅减少重复代码。

## 组件列表

| 组件 | 说明 |
|------|------|
| `MoneyCrud.js` | 核心类，管理 CRUD 状态与方法 |
| `MoneyCrudTable.vue` | 数据表格组件，支持分页、排序、列显隐 |
| `MoneyCUD.vue` | 顶部操作按钮组（新增/修改/删除） |
| `MoneyUD.vue` | 行内操作按钮（编辑/删除） |
| `MoneyForm.vue` | 表单弹窗组件 |
| `MoneyRR.vue` | 搜索栏组件 |

## 快速开始

### 基础用法

```vue
<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.name" placeholder="名称" @keyup.enter.native="moneyCrud.doQuery" />
        </MoneyRR>
        
        <!-- 操作行 -->
        <MoneyCUD :money-crud="moneyCrud" />
        
        <!-- 数据表格 -->
        <MoneyCrudTable :money-crud="moneyCrud">
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
            </template>
        </MoneyCrudTable>
        
        <!-- 表单弹窗 -->
        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="名称" prop="name">
                <el-input v-model.trim="moneyCrud.form.name" />
            </el-form-item>
        </MoneyForm>
    </PageWrapper>
</template>

<script setup>
import PageWrapper from "@/components/PageWrapper.vue";
import MoneyCrud from '@/components/crud/MoneyCrud.js';
import MoneyCrudTable from "@/components/crud/MoneyCrudTable.vue";
import MoneyRR from "@/components/crud/MoneyRR.vue";
import MoneyCUD from "@/components/crud/MoneyCUD.vue";
import MoneyUD from "@/components/crud/MoneyUD.vue";
import MoneyForm from "@/components/crud/MoneyForm.vue";
import { ref } from "vue";
import myApi from "@/api/example.js";

const columns = [
    { prop: 'name', label: '名称' },
    { prop: 'createTime', label: '创建时间', width: 180 },
    { prop: 'opt', label: '操作', width: 120, align: 'center', fixed: 'right', isMoneyUD: true }
];

const rules = {
    name: [{ required: true, message: '请输入名称' }]
};

const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: myApi
}));

moneyCrud.value.init(moneyCrud);
</script>
```

## MoneyCrud 核心类

### 配置项

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `columns` | `Array` | `[]` | 表格列配置 |
| `data` | `Array` | `[]` | 表格数据 |
| `isPage` | `Boolean` | `true` | 是否开启分页 |
| `page` | `Object` | `{ currentPage: 1, pageSize: 10, total: 100 }` | 分页配置 |
| `query` | `Object` | `{}` | 查询参数对象 |
| `defaultForm` | `Object` | `{}` | 默认表单对象（用于重置） |
| `form` | `Object` | `{}` | 表单对象 |
| `selections` | `Array` | `[]` | 选中行数据 |
| `defaultSort` | `Object` | `{}` | 默认排序配置 |
| `sort` | `Object` | `{}` | 当前排序配置 |
| `queryOnCreated` | `Boolean` | `true` | 初始化后是否自动查询 |
| `crudMethod` | `Object` | - | CRUD API 方法 |
| `optShow` | `Object` | `{ moneyRR: true, checkbox: true, add: true, edit: true, del: true }` | 操作按钮显示控制 |
| `rowOptDisabled` | `Object` | `{ checkbox: (row) => false, edit: (row) => false, del: (row) => false }` | 行操作禁用控制 |
| `msg` | `Object` | `{ ok: '操作成功', add: '新增成功', edit: '修改成功', del: '删除成功' }` | 提示消息 |

### 列配置 (columns)

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `prop` | `String` | - | 列字段名 |
| `label` | `String` | - | 列标题 |
| `width` | `String/Number` | - | 列宽度 |
| `minWidth` | `String/Number` | - | 最小列宽 |
| `align` | `String` | - | 对齐方式 |
| `fixed` | `String` | - | 固定列（left/right） |
| `showOverflowTooltip` | `Boolean` | `true` | 内容过长时显示 tooltip |
| `sortable` | `String` | `'custom'` | 排序方式 |
| `show` | `Boolean` | `true` | 是否显示该列 |
| `isMoneyUD` | `Boolean` | `false` | 是否为操作列 |

### CRUD 方法 (crudMethod)

需要提供以下 API 方法：

```javascript
{
    list: (query) => Promise,   // 查询列表，返回 { data: { records: [], current, size, total } }
    add: (form) => Promise,     // 新增
    edit: (form) => Promise,    // 编辑
    del: (ids) => Promise       // 删除（批量）
}
```

### 实例方法

| 方法 | 参数 | 说明 |
|------|------|------|
| `init(ref, doSomething?)` | ref, callback | 初始化 CRUD |
| `doQuery(notResetPage?)` | Boolean | 执行查询 |
| `reset()` | - | 重置查询条件并查询 |
| `toAdd()` | - | 打开新增弹窗 |
| `doAdd()` | - | 执行新增操作 |
| `toEdit(row)` | Object | 打开编辑弹窗 |
| `doEdit()` | - | 执行编辑操作 |
| `doDel(rows)` | Array | 执行删除操作 |
| `messageOk()` | - | 显示操作成功提示 |

### 状态 (state)

| 常量 | 值 | 说明 |
|------|-----|------|
| `STATE.NONE` | `'none'` | 无操作 |
| `STATE.ADD` | `'add'` | 新增状态 |
| `STATE.EDIT` | `'edit'` | 编辑状态 |

### 钩子函数 (Hook)

```javascript
Hook: {
    beforeDoQuery: (query) => null,    // 查询前
    afterDoQuery: (data) => null,      // 查询后
    beforeToAdd: (form) => null,       // 打开新增弹窗前
    beforeToEdit: (form) => null,      // 打开编辑弹窗前
    beforeDoAdd: (form) => null,       // 新增前
    afterDoAdd: (data) => null,        // 新增后
    beforeDoEdit: (form) => null,      // 编辑前
    afterDoEdit: (data) => null,       // 编辑后
    beforeDoDel: (ids) => null,        // 删除前
    afterDoDel: (ids) => null          // 删除后
}
```

#### 钩子使用示例

```javascript
// 编辑前转换数据格式
moneyCrud.value.Hook.beforeToEdit = (form) => {
    form.roles = form.roles.map(e => e.id);
};

// 查询后处理数据
moneyCrud.value.Hook.afterDoQuery = (data) => {
    console.log('查询完成', data);
};
```

## MoneyCrudTable 表格组件

### Props

| 属性 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `moneyCrud` | `MoneyCrud` | 是 | CRUD 实例 |

### 插槽

| 插槽名 | 参数 | 说明 |
|--------|------|------|
| `[prop]` | `{ scope }` | 自定义列内容，prop 为列字段名 |

### 列自定义示例

```vue
<MoneyCrudTable :money-crud="moneyCrud">
    <template #status="{scope}">
        <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
        </el-tag>
    </template>
    <template #opt="{scope}">
        <MoneyUD :money-crud="moneyCrud" :scope="scope" />
    </template>
</MoneyCrudTable>
```

## MoneyCUD 操作按钮组

### Props

| 属性 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `moneyCrud` | `MoneyCrud` | 是 | CRUD 实例 |

### 插槽

| 插槽名 | 说明 |
|--------|------|
| `default` | 自定义按钮 |

### 功能按钮

- **新增**：打开新增弹窗
- **修改**：编辑选中行（单选）
- **删除**：删除选中行（批量）
- **搜索**：切换搜索栏显示
- **刷新**：重新查询
- **列设置**：自定义显示列

## MoneyUD 行操作按钮

### Props

| 属性 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `moneyCrud` | `MoneyCrud` | 是 | - | CRUD 实例 |
| `scope` | `Object` | 是 | - | 表格行 scope 对象 |
| `delConfirmMsg` | `String` | 否 | `'确定删除本条数据吗？'` | 删除确认提示 |

## MoneyForm 表单弹窗

### Props

| 属性 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `moneyCrud` | `MoneyCrud` | 是 | - | CRUD 实例 |
| `dialogClass` | `String` | 否 | `'!w-11/12 md:!w-1/2 lg:!w-1/3'` | 弹窗样式类 |
| `rules` | `Object` | 否 | - | 表单验证规则 |

### 插槽

| 插槽名 | 说明 |
|--------|------|
| `default` | 表单项内容 |

## MoneyRR 搜索栏

### Props

| 属性 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `moneyCrud` | `MoneyCrud` | 是 | CRUD 实例 |

### 插槽

| 插槽名 | 说明 |
|--------|------|
| `default` | 搜索表单项 |
| `opt` | 自定义操作按钮 |

### 示例

```vue
<MoneyRR :money-crud="moneyCrud">
    <el-input v-model="moneyCrud.query.name" placeholder="名称" />
    <el-select v-model="moneyCrud.query.status" clearable placeholder="状态">
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
    </el-select>
</MoneyRR>
```

## 完整示例

```vue
<template>
    <PageWrapper>
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.name" placeholder="用户名" @keyup.enter.native="moneyCrud.doQuery" />
            <el-select v-model="moneyCrud.query.enabled" clearable placeholder="状态">
                <el-option v-for="item in [true, false]" :key="item" :label="item ? '启用':'禁用'" :value="item" />
            </el-select>
        </MoneyRR>
        
        <MoneyCUD :money-crud="moneyCrud" />
        
        <MoneyCrudTable :money-crud="moneyCrud">
            <template #roles="{scope}">
                <el-tag type="warning">{{ scope.row.roles[0].roleName }}</el-tag>
            </template>
            <template #enabled="{scope}">
                <el-switch v-model="scope.row.enabled" @change="changeEnabled(scope.row)" />
            </template>
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
            </template>
        </MoneyCrudTable>
        
        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="用户名" prop="username">
                <el-input v-model.trim="moneyCrud.form.username" :disabled="moneyCrud.state === moneyCrud.STATE.EDIT" />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
                <el-input v-model.trim="moneyCrud.form.nickname" />
            </el-form-item>
            <el-form-item label="状态">
                <el-radio-group v-model="moneyCrud.form.enabled">
                    <el-radio :value="true">启用</el-radio>
                    <el-radio :value="false">禁用</el-radio>
                </el-radio-group>
            </el-form-item>
        </MoneyForm>
    </PageWrapper>
</template>

<script setup>
import PageWrapper from "@/components/PageWrapper.vue";
import MoneyCrud from '@/components/crud/MoneyCrud.js';
import MoneyCrudTable from "@/components/crud/MoneyCrudTable.vue";
import MoneyRR from "@/components/crud/MoneyRR.vue";
import MoneyCUD from "@/components/crud/MoneyCUD.vue";
import MoneyUD from "@/components/crud/MoneyUD.vue";
import MoneyForm from "@/components/crud/MoneyForm.vue";
import { ref } from "vue";
import userApi from "@/api/system/user.js";

const columns = [
    { prop: 'username', label: '用户名' },
    { prop: 'nickname', label: '昵称' },
    { prop: 'roles', label: '角色', align: 'center' },
    { prop: 'enabled', label: '状态', align: 'center' },
    { prop: 'createTime', label: '创建时间', width: 180, show: false },
    { prop: 'opt', label: '操作', width: 120, align: 'center', fixed: 'right', isMoneyUD: true }
];

const rules = {
    username: [{ required: true, message: '请输入用户名' }],
    nickname: [{ required: true, message: '请输入昵称' }]
};

const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: userApi,
    defaultForm: { enabled: true },
    optShow: {
        checkbox: true,
        add: true,
        edit: true,
        del: true
    },
    rowOptDisabled: {
        checkbox: (row) => row.isAdmin,
        edit: (row) => row.isAdmin,
        del: (row) => row.isAdmin
    }
}));

moneyCrud.value.init(moneyCrud);

moneyCrud.value.Hook.beforeToEdit = (form) => {
    form.roles = form.roles.map(e => e.id);
};

function changeEnabled(row) {
    userApi.edit({ id: row.id, enabled: row.enabled }).then(() => {
        moneyCrud.value.messageOk();
    });
}
</script>
```

## 注意事项

1. **API 返回格式**：`list` 方法需返回 `{ data: { records: [], current, size, total } }` 格式的分页数据
2. **主键字段**：默认使用 `id` 作为主键，删除时会提取 `id` 数组
3. **权限控制**：通过 `optShow` 和 `rowOptDisabled` 控制按钮显示和禁用
4. **表单验证**：使用 Element Plus 的表单验证规则
