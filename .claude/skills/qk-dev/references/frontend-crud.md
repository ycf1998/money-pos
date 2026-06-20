# 前端 CRUD 约定

## API 模块

路径：`src/api/{module}/{entity}.js`（系统模块用 `system`，业务模块按需命名）

```javascript
import req from '@/api/index.js'
export default {
    list: (query) => req({ url: '/path', method: 'GET', params: query }),
    add: (data) => req({ url: '/path', method: 'POST', data }),
    edit: (data) => req({ url: '/path', method: 'PUT', data }),
    del: (ids) => req({ url: '/path', method: 'DELETE', data: ids }),
}
```

- 用 `req`（封装了 axios），路径不加 `/api` 前缀
- `list` 用 `params`，其余用 `data`
- `del` 直接传 `ids` 数组

## 视图页面

路径：`src/views/{module}/{entity}/index.vue`

### 固定导入

```javascript
import PageWrapper from '@/components/PageWrapper.vue'
import MoneyCrud from '@/components/crud/MoneyCrud.js'
import MoneyCrudTable from '@/components/crud/MoneyCrudTable.vue'
import MoneyRR from '@/components/crud/MoneyRR.vue'
import MoneyCUD from '@/components/crud/MoneyCUD.vue'
import MoneyUD from '@/components/crud/MoneyUD.vue'
import MoneyForm from '@/components/crud/MoneyForm.vue'
import { ref } from 'vue'
import api from '@/api/system/entity.js'
import { useUserStore } from '@/store/index.js'
```

### 模板结构（五组件）

```html
<PageWrapper>
    <MoneyRR :money-crud="moneyCrud">
        <!-- el-input / el-select v-model="moneyCrud.query.xxx" -->
    </MoneyRR>
    <MoneyCUD :money-crud="moneyCrud" />
    <MoneyCrudTable :money-crud="moneyCrud">
        <!-- 自定义列：<template #prop="{scope}"> -->
        <template #opt="{scope}">
            <MoneyUD :money-crud="moneyCrud" :scope="scope" />
        </template>
    </MoneyCrudTable>
    <MoneyForm :money-crud="moneyCrud" :rules="rules">
        <!-- el-form-item prop="xxx" → v-model="moneyCrud.form.xxx" -->
    </MoneyForm>
</PageWrapper>
```

### Script 关键配置

**列定义**：
```javascript
const columns = [
    { prop: 'name', label: '名称' },
    { prop: 'createTime', label: '创建时间', width: 180 },
    { prop: 'opt', label: '操作', width: 120, align: 'center', fixed: 'right', isMoneyUD: true },
]
```

**校验规则**：Element Plus 标准 rules，message 用中文。

**MoneyCrud 实例**：
```javascript
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: api,
    defaultForm: { /* 新增时的默认值 */ },
    optShow: {
        checkbox: userStore.hasPermission(['entity:edit', 'entity:del']),
        add: userStore.hasPermission('entity:add'),
        edit: userStore.hasPermission('entity:edit'),
        del: userStore.hasPermission('entity:del'),
    },
    rowOptDisabled: {
        checkbox: (row) => /* 条件 */,
        edit: (row) => /* 条件 */,
        del: (row) => /* 条件 */,
    },
}))
moneyCrud.value.init(moneyCrud)
```

### 钩子

```javascript
// 编辑前：关联对象 → ID 数组
moneyCrud.value.Hook.beforeToEdit = (form) => {
    form.roleIds = form.roles?.map(r => r.id) || []
}
```

## 常见列模板

| 场景 | 代码 |
|------|------|
| 状态标签 | `<el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>` |
| 开关切换 | `<el-switch v-model="row.enabled" @change="api.edit({id: row.id, enabled: row.enabled})" />` |
| 关联对象取值 | `{{ row.category?.name }}` |
| 编辑时禁用输入 | `:disabled="moneyCrud.state === moneyCrud.STATE.EDIT"` |

> MoneyCrud 完整配置项详见 `src/components/crud/README.md`
