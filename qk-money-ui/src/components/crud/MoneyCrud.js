import { useGlobalProp } from "@/composables/globalProp.js";

class MoneyCrud {
    _ref = null;
    isInit = false;

    // 列
    columns = [
        {
            prop: 'demo',
            label: 'Demo',
            width: '',
            minWidth: '',
            align: '',
            fixed: '',
            showOverflowTooltip: true,
            sortable: 'custom',
            show: true,
            isMoneyUD: false,
        },
    ];

    // 数据
    data = [];

    // 分页
    isPage = true;
    page = { currentPage: 1, pageSize: 10, total: 100 };

    // 查询对象
    query = {};

    // 默认表单对象（用于重置表单）
    defaultForm = {};

    // 表单对象
    form = {};

    // 选中项
    selections = [];

    // 默认排序
    defaultSort = {};

    // 排序
    sort = {};

    // 加载后立即查询
    queryOnCreated = true;

    // CRUD 方法
    crudMethod = {
        list: (query) => Promise.reject('未初始化 list 方法'),
        add: (form) => Promise.reject('未初始化 add 方法'),
        edit: (form) => Promise.reject('未初始化 edit 方法'),
        del: (ids) => Promise.reject('未初始化 del 方法'),
    };

    // 操作显示
    optShow = {
        moneyRR: true,
        checkbox: true,
        add: true,
        edit: true,
        del: true,
    };

    // 行操作可用
    rowOptDisabled = {
        checkbox: (row) => false,
        edit: (row) => false,
        del: (row) => false,
    };

    // 消息提示
    msg = {
        ok: '操作成功',
        add: '新增成功',
        edit: '修改成功',
        del: '删除成功',
    };

    // 状态
    state = 'none';
    STATE = {
        NONE: 'none',
        ADD: 'add',
        EDIT: 'edit',
    };

    // Hook 钩子
    Hook = {
        beforeDoQuery: (query) => null, // 查询前
        afterDoQuery: (data) => null, // 查询后
        beforeToAdd: (form) => null, // 打开新增弹窗前
        beforeToEdit: (form) => null, // 打开编辑弹窗前
        beforeDoAdd: (form) => null, // 新增前
        afterDoAdd: (data) => null, // 新增后
        beforeDoEdit: (form) => null, // 编辑前
        afterDoEdit: (data) => null, // 编辑后
        beforeDoDel: (ids) => null, // 删除前
        afterDoDel: (ids) => null, // 删除后
    };

    $message = useGlobalProp().$message;
    $confirm = useGlobalProp().$confirm;

    constructor(option) {
        Object.entries(option).forEach(([key, value]) => {
            if (typeof this[key] === 'object') {
                Object.assign(this[key], value);
            } else {
                this[key] = value;
            }
        });

        // 如果列中有 isMoneyUD 且 optShow.edit 和 optShow.del 都为 false，则隐藏该列
        const opt = this.columns.find((e) => e.isMoneyUD);
        if (opt && !this.optShow.edit && !this.optShow.del) opt.show = false;
    }

    async init(ref, doSomething) {
        this._ref = ref;
        if (doSomething) await doSomething();
        if (this.queryOnCreated) await this.doQuery(null);
        this.ref().isInit = true;
    }

    ref = () => this._ref.value;

    // 查询（保持非 async）
    doQuery = (notResetPage) => {
        if (!this.crudMethod.list) {
            this.Hook.afterDoQuery();
            return;
        }

        if (this.page.currentPage !== 1 && !notResetPage) this.ref().page.currentPage = 1;

        const pageQuery = this.isPage
            ? { page: this.page.currentPage, size: this.page.pageSize }
            : {};
        const sortQuery = this.sort.prop
            ? { orderBy: `${this.sort.prop},${this.sort.order}` }
            : {};
        const query = { ...pageQuery, ...this.query, ...sortQuery };

        this.Hook.beforeDoQuery(query);

        return this.crudMethod.list(query)
            .then((res) => {
                if (this.isPage) {
                    this.ref().data = res.data.records;
                    this.ref().page = {
                        currentPage: res.data.current,
                        pageSize: res.data.size,
                        total: res.data.total,
                    };
                } else {
                    this.ref().data = res.data;
                }
                this.Hook.afterDoQuery(res.data);
            })
            .catch(() => {
                this.Hook.afterDoQuery(null);
            });
    };

    // 重置
    reset = () => {
        Object.keys(this.ref().query).forEach((k) => delete this.ref().query[k]);
        this.doQuery();
    };

    // 新增
    toAdd = () => {
        this.ref().form = { ...this.defaultForm };
        this.ref().state = this.STATE.ADD;
        this.Hook.beforeToAdd(this.ref().form);
    };

    doAdd = () => {
        this.Hook.beforeDoAdd(this.ref().form);
        this.crudMethod.add(this.ref().form)
            .then((res) => {
                this.$message.success(this.msg.add);
                this.Hook.afterDoAdd(res.data);
                this.doQuery();
            });
    };

    // 编辑
    toEdit = (row) => {
        this.ref().form = { ...row };
        this.ref().state = this.STATE.EDIT;
        this.Hook.beforeToEdit(this.ref().form);
    };

    doEdit = () => {
        this.Hook.beforeDoEdit(this.ref().form);
        this.crudMethod.edit(this.ref().form)
            .then((res) => {
                this.$message.success(this.msg.edit);
                this.Hook.afterDoEdit(res.data);
                this.doQuery();
            });
    };

    // 删除
    doDel = (rows) => {
        const ids = rows.map((e) => e.id);
        this.Hook.beforeDoDel(ids);
        this.crudMethod.del(ids)
            .then(() => {
                this.$message.success(this.msg.del);
                this.Hook.afterDoDel(ids);
                this.doQuery();
            });
    };

    /**
     * 单选
     * @param currentRow 当前选中行
     * @param oldCurrentRow 上一次选中行
     */
    currentChange = (currentRow, oldCurrentRow) => {
        if (this.optShow.checkbox) return
        this.ref().selections = [currentRow]
    }
    /**
     * 多选
     * @param rows 选中行
     */
    selectionChange = (rows) => {
        if (!this.optShow.checkbox) return
        this.ref().selections = rows
    }
    /**
     * 排序
     * @param column 排序列
     * @param prop 排序参数
     * @param order 排序方式
     */
    doSort = ({ column, prop, order }) => {
        if (column.sortable !== 'custom') {
            // 前端排序
            return
        }
        if (order) {
            order = (order === 'descending' ? 'desc' : 'asc')
            this.sort = { prop, order }
        } else {
            this.sort = {}
        }
        this.doQuery()
    }
    /**
     * current-page 改变时触发
     * @param currentPage 当前页
     */
    currentPageChange = (currentPage) => {
        this.ref().page.currentPage = currentPage
        this.doQuery(true)
    }
    /**
     * page-size 改变时触发
     * @param pageSize 页大小
     */
    pageSizeChange = (pageSize) => {
        this.ref().page.pageSize = pageSize
        this.doQuery()
    }

    /**
     * 操作成功提示
     */
    messageOk = () => this.$message.success(this.msg.ok)
}

export default MoneyCrud