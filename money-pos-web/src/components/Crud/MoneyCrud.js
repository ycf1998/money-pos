import {useGlobalProp} from "@/composables/globalProp.js";

class MoneyCrud {
    _ref = null
    isInit = false
    // 列
    columns = [{
        prop: 'demo',
        label: 'Demo',
        width: '',
        minWidth: '',
        align: '',
        fixed: '',
        showOverflowTooltip: true,
        sortable: 'custom',
        show: true,
        isMoneyUD: false
    }]
    // 数据
    data = []
    // 分页
    isPage = true
    page = {currentPage: 1, pageSize: 10, total: 100}

    // 查询对象
    query = {}
    // 默认表单对象（用于重置表单）
    defaultForm = {}
    // 表单对象
    form = {}
    // 选中项
    selections = []
    // 默认排序 { prop: 'date', order: 'descending' }
    defaultSort = {}
    // 排序
    sort = {}

    // 加载后里面查询
    queryOnCreated = true
    // curd 方法
    crudMethod = {
        list: query => {
            return new Promise((resolve, reject) => reject('未初始化该方法'))
        },
        add: form => {
            return new Promise((resolve, reject) => reject('未初始化该方法'))
        },
        edit: form => {
            return new Promise((resolve, reject) => reject('未初始化该方法'))
        },
        del: ids => {
            return new Promise((resolve, reject) => reject('未初始化该方法'))
        },
    }
    // 操作显示
    optShow = {
        moneyRR: true,
        checkbox: true,
        add: true,
        edit: true,
        del: true
    }
    // 行操作可用
    rowOptDisabled = {
        checkbox: (row) => false,
        edit: (row) => false,
        del: (row) => false
    }
    // 消息提示
    msg = {
        ok: '操作成功',
        add: '新增成功',
        edit: '修改成功',
        del: '删除成功'
    }

    // 状态
    state = 'none'
    STATE = {
        NONE: 'none',
        ADD: 'add',
        EDIT: 'edit'
    }

    // Hook 钩子
    Hook = {
        /** 执行查询后 **/
        afterDoQuery: (data) => null,
        /** 打开新增弹窗前 **/
        beforeToAdd: (form) => null,
        /** 打开修改弹窗前 **/
        beforeToEdit: (form) => null,
    }

    $message = useGlobalProp().$message
    $confirm = useGlobalProp().$confirm


    constructor(option) {
        Object.entries(option).forEach(kv => {
            if (typeof this[kv[0]] === 'object') {
                Object.assign(this[kv[0]], kv[1])
            } else {
                this[kv[0]] = kv[1]
            }
        })

        const opt = this.columns.find(e => e.isMoneyUD)
        if (opt && !this.optShow.edit && !this.optShow.del) opt.show = false
    }

    async init(ref, doSomething) {
        this._ref = ref
        if (doSomething) await doSomething()
        if (this.queryOnCreated) await this.doQuery(null, true)
        this.ref().isInit = true
    }

    ref = () => this._ref.value

    /**
     * 查询
     * @param notResetPage
     */
    doQuery = (notResetPage) => {
        if (this.crudMethod.list) {
            if (this.page.currentPage !== 1 && !notResetPage) this.ref().page.currentPage = 1
            // TODO Money 根据后端调整分页、排序或返回值的字段
            const pageQuery = {
                page: this.page.currentPage,
                size: this.page.pageSize,
            }
            const sortQuery = this.sort.prop ? {orderBy: `${this.sort.prop},${this.sort.order}`} : {}
            const query = this.isPage ? Object.assign({}, pageQuery, this.query, sortQuery) : Object.assign({}, this.query, sortQuery)
            return this.crudMethod.list(query).then(res => {
                if (this.isPage) {
                    this.ref().data = res.data.records
                    this.ref().page = {
                        currentPage: res.data.current,
                        pageSize: res.data.size,
                        total: res.data.total
                    }
                } else {
                    this.ref().data = res.data
                }
                this.Hook.afterDoQuery(res.data)
            })
        }
        this.Hook.afterDoQuery()
    }
    /**
     * 重置
     */
    reset = () => {
        Object.keys(this.ref().query).forEach(k => delete this.ref().query[k])
        this.doQuery()
    }
    toAdd = () => {
        this.ref().form = {}
        Object.assign(this.ref().form, this.defaultForm)
        this.ref().state = this.STATE.ADD
        this.Hook.beforeToAdd(this.ref().form)
    }
    doAdd = async () => {
        return this.crudMethod.add(this.form)
            .then(() => {
                this.$message.success(this.msg.add)
                this.doQuery()
            })
    }
    toEdit = (row) => {
        this.ref().form = {}
        Object.assign(this.ref().form, row)
        this.ref().state = this.STATE.EDIT
        this.Hook.beforeToEdit(this.ref().form)
    }
    doEdit = async () => {
        return this.crudMethod.edit(this.form)
            .then(() => {
                this.$message.success(this.msg.edit)
                this.doQuery()
            })
    }
    doDel = (rows) => {
        return this.crudMethod.del(rows.map(e => e.id))
            .then(() => {
                this.$message.success(this.msg.del)
                this.doQuery()
                return true
            })
    }

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
    doSort = ({column, prop, order}) => {
        if (column.sortable !== 'custom') {
            // 前端排序
            return
        }
        if (order) {
            order = (order === 'descending' ? 'desc' : 'asc')
            this.sort = {prop, order}
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