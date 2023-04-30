package com.money.common.response;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 响应结果
 * @createTime : 2022-01-01 13:26:55
 */
public enum RStatus implements IStatus {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    BAD_REQUEST(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "找不到页面");

    /**
     * 代码
     */
    final int code;

    /**
     * 消息
     */
    final String message;

    RStatus(int code, String message) {
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
