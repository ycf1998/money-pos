# 前端可复用组件

> 本文件是给 agent 的操作规则（路径、何时用、关键约束）。组件的完整用法与 API 手册在 `doc/frontend/` 与组件源码目录，不在此重复。

## 布局组件

### PageWrapper

路径：`@/components/PageWrapper.vue`

页面外层容器。提供统一的背景色、圆角、内边距、响应式高度。所有页面内容包在其中。

```html
<PageWrapper>
    <!-- 页面内容 -->
</PageWrapper>
```

props：`customClass`（额外 CSS 类）。

---

## CRUD 组件族

写列表页一律使用，不手写表格。**动手规范见 `frontend-crud.md`**，人读用法手册见 `doc/frontend/MoneyCrud.md`，完整 API 见 `src/components/crud/README.md`。

| 文件 | 说明 |
|---|---|
| `@/components/crud/MoneyCrud.js` | 核心类：表格数据、分页、表单、选中行 |
| `@/components/crud/MoneyCrudTable.vue` | 数据表格 + 分页 |
| `@/components/crud/MoneyCUD.vue` | 顶部工具栏（新增 / 修改 / 删除 + 搜索切换 / 刷新 / 列设置） |
| `@/components/crud/MoneyUD.vue` | 行内操作列（修改 / 删除） |
| `@/components/crud/MoneyForm.vue` | 新增 / 编辑弹窗表单 |
| `@/components/crud/MoneyRR.vue` | 搜索栏容器 |

要点：

- 实例化后必须调用 `moneyCrud.value.init(moneyCrud)`；首个参数传 ref 自身。
- 操作列在 `columns` 中标记 `isMoneyUD: true`，在 `MoneyCrudTable` 的 `#opt` 插槽放 `MoneyUD`。
- 按钮显隐 / 禁用用 `optShow`、`rowOptDisabled`，取值来自 `useUserStore().hasPermission(...)`。
- 编辑前把关联对象转成 ID 用钩子：`moneyCrud.value.Hook.beforeToEdit = (form) => { form.roleIds = form.roles?.map(r => r.id) || [] }`。

---

## 通用组件

### SvgIcon

路径：`@/components/SvgIcon.vue`（已全局注册）

```html
<SvgIcon name="user" dir="system" class="w-5 h-5" />
```

props 与图标目录约定见 `doc/frontend/全局svg.md`。

### IconSelect

路径：`@/components/IconSelect.vue`（未全局注册，需显式 import）

图标选择器，列出项目内全部 SVG 图标供选择。用法与 props 见 `doc/frontend/全局svg.md`。

### ComputeInput

路径：`@/components/ComputeInput.vue`

支持简单四则运算的输入框。用户输入 `100+50` 按回车自动计算结果。

---

## 全局注册

以下 Element Plus 图标组件已在全局注册，**无需导入**，直接使用：

`<Edit />` `<Delete />` `<Search />` `<Refresh />` `<Operation />` `<Plus />` `<Minus />` `<Close />` `<Check />` `<ArrowDown />` `<ArrowUp />` 等全部 EP 图标

`<SvgIcon />` 组件也是全局注册的，无需导入。
