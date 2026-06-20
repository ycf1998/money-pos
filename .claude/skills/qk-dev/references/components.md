# 前端可复用组件

## 布局组件

### PageWrapper

路径：`@/components/PageWrapper.vue`

页面外层容器。提供统一的背景色、圆角、内边距、响应式高度。

```html
<PageWrapper>
    <!-- 页面内容 -->
</PageWrapper>
```

props：`customClass`（额外 CSS 类）

---

## CRUD 组件族

### MoneyCrud（核心类）

路径：`@/components/crud/MoneyCrud.js`

CRUD 状态机，管理表格数据、分页、表单、选中行。所有 CRUD 操作的核心。

**关键配置**：

| 属性 | 说明 |
|------|------|
| `columns` | 列定义数组 |
| `crudMethod` | API 模块（含 list/add/edit/del） |
| `query` | 查询参数对象（v-model 绑定搜索输入） |
| `defaultForm` | 新增时的默认表单值 |
| `optShow` | 按权限控制按钮/搜索栏显隐 |
| `rowOptDisabled` | 按行条件控制操作禁用 |

**关键方法**：`init(ref, callback?)` / `doQuery()` / `toAdd()` / `toEdit(row)` / `doDel(rows)`

**Hook 点**：`beforeToEdit`（最常用，把关联对象转 ID）、`beforeDoAdd`、`beforeDoEdit`、`afterDoQuery`

### MoneyCrudTable

路径：`@/components/crud/MoneyCrudTable.vue`

数据表格 + 分页。通过命名 slot 自定义列内容。

### MoneyCUD

路径：`@/components/crud/MoneyCUD.vue`

顶部工具栏：新增/编辑/删除按钮 + 搜索切换 + 刷新 + 列设置。

### MoneyUD

路径：`@/components/crud/MoneyUD.vue`

行内操作按钮：编辑 + 删除（带确认弹窗）。

### MoneyForm

路径：`@/components/crud/MoneyForm.vue`

新增/编辑弹窗表单。自动识别 STATE 显示"新增"或"编辑"标题。提交时调用 `moneyCrud.doAdd()` 或 `moneyCrud.doEdit()`。

### MoneyRR

路径：`@/components/crud/MoneyRR.vue`

搜索栏容器。default 插槽放搜索控件，opt 插槽自定义按钮（默认搜索/重置）。

---

## 通用组件

### SvgIcon

路径：`@/components/SvgIcon.vue`

渲染 SVG 图标。

```html
<SvgIcon name="user" dir="system" class="w-5 h-5" />
```

props：
- `name` — 图标名（必需）
- `dir` — 图标目录/前缀，默认无
- `class` — CSS 类，默认 `w-6 h-6`
- `fill` — 填充色，默认 `currentColor`

### IconSelect

路径：`@/components/IconSelect.vue`

图标选择器，列出项目中所有可用 SVG 图标供选择。

```html
<IconSelect dir="open" @selected="icon => form.icon = icon" />
```

### ComputeInput

路径：`@/components/ComputeInput.vue`

支持简单四则运算的输入框。用户输入 `100+50` 按回车自动计算结果。

---

## 全局注册

以下 Element Plus 图标组件已在全局注册，**无需导入**，直接使用：

`<Edit />` `<Delete />` `<Search />` `<Refresh />` `<Operation />` `<Plus />` `<Minus />` `<Close />` `<Check />` `<ArrowDown />` `<ArrowUp />` 等全部 EP 图标

`<SvgIcon />` 组件也是全局注册的，无需导入。
