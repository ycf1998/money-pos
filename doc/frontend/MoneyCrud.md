# MoneyCrud

一套基于 Vue 3 + Element Plus 的 CRUD 快速开发组件。列表页统一用它，不手写表格。

## 组成

| 文件 | 说明 |
|---|---|
| [MoneyCrud.js](../../qk-money-ui/src/components/crud/MoneyCrud.js) | 核心类，管理数据、分页、表单、选中行 |
| [MoneyCrudTable.vue](../../qk-money-ui/src/components/crud/MoneyCrudTable.vue) | 数据表格 + 分页 |
| [MoneyCUD.vue](../../qk-money-ui/src/components/crud/MoneyCUD.vue) | 顶部操作栏（新增 / 修改 / 删除 + 搜索切换、刷新、列设置） |
| [MoneyUD.vue](../../qk-money-ui/src/components/crud/MoneyUD.vue) | 行内操作列（修改 / 删除） |
| [MoneyForm.vue](../../qk-money-ui/src/components/crud/MoneyForm.vue) | 新增 / 编辑弹窗表单 |
| [MoneyRR.vue](../../qk-money-ui/src/components/crud/MoneyRR.vue) | 搜索栏容器 |

## 快速开始

以 `src/views/system/user/index.vue` 为例：

```vue
<template>
    <PageWrapper>
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.name" placeholder="用户名/昵称"
                      class="md:w-48!" @keyup.enter="moneyCrud.doQuery" />
        </MoneyRR>

        <MoneyCUD :money-crud="moneyCrud" />

        <MoneyCrudTable :money-crud="moneyCrud">
            <template #enabled="{scope}">
                <el-switch v-model="scope.row.enabled" @change="changeEnabled(scope.row)" />
            </template>
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
            </template>
        </MoneyCrudTable>

        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="用户名" prop="username">
                <el-input v-model.trim="moneyCrud.form.username"
                          :disabled="moneyCrud.state === moneyCrud.STATE.EDIT" />
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
import { useUserStore } from "@/store/index.js";
import userApi from "@/api/system/user.js";

const userStore = useUserStore()

const columns = [
    { prop: 'username', label: '用户名' },
    { prop: 'createTime', label: '创建时间', width: 180, show: false },
    { prop: 'opt', label: '操作', width: 150, align: 'center', fixed: 'right', isMoneyUD: true },
]

const rules = {
    username: [{ required: true, message: '请输入用户名' }],
}

const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: userApi,
    optShow: {
        add: userStore.hasPermission('user:add'),
        edit: userStore.hasPermission('user:edit'),
        del: userStore.hasPermission('user:del'),
    },
}))

moneyCrud.value.init(moneyCrud)
</script>
```

## 组件与插槽

| 组件 | props | 插槽 |
|---|---|---|
| `MoneyRR` | `moneyCrud` | `default`：搜索控件；`opt`：自定义按钮（默认搜索 / 重置） |
| `MoneyCUD` | `moneyCrud` | `default`：自定义按钮 |
| `MoneyCrudTable` | `moneyCrud` | `[prop]="{ scope }"`：按列字段名自定义列内容 |
| `MoneyUD` | `moneyCrud`、`scope`、`delConfirmMsg` | — |
| `MoneyForm` | `moneyCrud`、`dialogClass`、`rules` | `default`：表单项，`v-model` 绑定 `moneyCrud.form.xxx` |

操作列需在 `columns` 中声明 `isMoneyUD: true`，并在 `MoneyCrudTable` 中用 `#opt` 插槽放入 `MoneyUD`。

## 关键配置

| 属性 | 说明 |
|---|---|
| `columns` | 列定义数组（`prop` / `label` / `width` / `fixed` / `show` / `sortable` / `isMoneyUD` 等） |
| `crudMethod` | API 模块，需含 `list` / `add` / `edit` / `del` |
| `query` | 查询参数对象，搜索控件 `v-model` 绑定到这里 |
| `defaultForm` | 新增时的默认表单值 |
| `optShow` | 控制按钮 / 搜索栏显隐，值常取自 `hasPermission` |
| `rowOptDisabled` | 按行控制操作禁用，值为 `(row) => boolean` |
| `isPage` | 是否分页，默认 `true` |
| `queryOnCreated` | 初始化后是否自动查询，默认 `true` |
| `msg` | 各操作的提示文案 |

`crudMethod` 约定：`list(query)` 返回 `{ data: { records, current, size, total } }`（分页时）；`add(form)` / `edit(form)` 传表单对象；`del(ids)` 传 id 数组。

筛选与分页参数由类自动组装：`page` / `size`、`orderBy`（排序时）。

## 初始化与生命周期

```js
moneyCrud.value.init(moneyCrud, doSomething?)
```

- `init` 的第一个参数传 `moneyCrud` 自身（ref）。
- 可选 `doSomething` 回调在首次查询前执行，用于加载下拉数据等前置请求。
- `queryOnCreated` 为 `true` 时，`init` 内自动发起首次 `doQuery`。

## 常用方法

| 方法 | 说明 |
|---|---|
| `doQuery(notResetPage?)` | 执行查询，默认回到第 1 页 |
| `reset()` | 清空查询条件并查询 |
| `toAdd()` / `toEdit(row)` | 打开新增 / 编辑弹窗 |
| `doAdd()` / `doEdit()` / `doDel(rows)` | 提交新增 / 编辑 / 删除 |
| `messageOk()` | 显示「操作成功」提示（用于表格内联操作后的反馈） |

## 状态与表单

- `state`：`STATE.NONE` / `STATE.ADD` / `STATE.EDIT`。`MoneyForm` 依此显示「新增」或「编辑」标题。
- `form`：当前表单对象，弹窗内的输入 `v-model` 绑定到 `moneyCrud.form.xxx`。
- `selections`：选中行数组，供 `MoneyCUD` 的修改 / 删除按钮使用。

## Hook

在实例上覆盖对应函数即可介入流程：

```js
moneyCrud.value.Hook.beforeToEdit = (form) => {
    form.roles = form.roles.map(e => e.id)
}
```

| 钩子 | 时机 |
|---|---|
| `beforeDoQuery` / `afterDoQuery` | 查询前 / 后 |
| `beforeToAdd` / `beforeToEdit` | 打开新增 / 编辑弹窗前 |
| `beforeDoAdd` / `afterDoAdd` | 提交新增前 / 后 |
| `beforeDoEdit` / `afterDoEdit` | 提交编辑前 / 后 |
| `beforeDoDel` / `afterDoDel` | 删除前 / 后 |

## 权限

按钮显隐与禁用通过 `optShow` / `rowOptDisabled` 结合 `useUserStore().hasPermission()` 实现，详见 [权限封装](./权限封装.md)。

## 完整 API

配置项、列配置、方法、状态、Hook 的完整清单见源码目录下的参考文档：[qk-money-ui/src/components/crud/README.md](../../qk-money-ui/src/components/crud/README.md)。
