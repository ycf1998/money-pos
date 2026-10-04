package com.money.constant;

import com.money.web.response.IStatus;

/**
 * 业务模块错误状态（6 位：1 类型 + 2 模块 + 3 错误码）
 *
 * <p>号段说明：
 * <pre>
 * 1xxxxx 数据类           — 不存在、已存在等
 * 2xxxxx 业务规则类        — 状态不允许、关联约束等
 * 3xxxxx ~ 9xxxxx 预留
 *
 * 中间 2 位模块：00=通用，01=商品(gms)，02=采购(pms)，...
 * 后 3 位错误码：模块内递增
 * </pre>
 *
 * @author : money
 * @since : 1.0.0
 */
public enum BizErrorStatus implements IStatus {

    // ======== 1xxxxx 数据类 · 通用(00) ========
    DATA_NOT_FOUND(100000, "数据不存在"),
    DATA_ALREADY_EXIST(100001, "数据已存在"),

    // ======== 2xxxxx 业务规则 · 通用(00) ========
    STATUS_NOT_ALLOWED(200000, "当前状态不允许此操作"),
    REF_EXISTS(200001, "存在关联数据，不可操作"),

    ;

    final int code;
    final String message;

    BizErrorStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
