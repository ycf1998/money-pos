package com.money.common.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 统一响应返回
 * @createTime : 2022-01-01 13:26:48
 */
@Getter
@Setter
@AllArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = -9068318251197180009L;

    @Schema(description = "返回码")
    private int code;

    @Schema(description = "返回信息")
    private String message;

    @Schema(description = "返回数据")
    private T data;
    

    /**
     * 成功
     *
     * @return {@link R}<{@link String}>
     */
    public static <T> R<T> success() {
        return new R<>(RStatus.SUCCESS.getCode(), RStatus.SUCCESS.getMessage(), null);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> success(T data) {
        return new R<>(RStatus.SUCCESS.getCode(), RStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 成功
     *
     * @param message 消息
     * @param data    数据
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> success(String message, T data) {
        return new R<>(RStatus.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败
     *
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> fail() {
        return new R<>(RStatus.FAILED.getCode(), RStatus.FAILED.getMessage(), null);
    }

    /**
     * 失败
     *
     * @param message 消息
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> fail(String message) {
        return new R<>(RStatus.FAILED.getCode(), message, null);
    }

    /**
     * 失败
     * 失败
     *
     * @param code    代码
     * @param message 消息
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    /**
     * 参数校验失败
     *
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> validateFailed() {
        return fail(RStatus.BAD_REQUEST.getCode(), RStatus.BAD_REQUEST.getMessage());
    }

    /**
     * 参数校验失败
     *
     * @param message 消息
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> validateFailed(String message) {
        return new R<>(RStatus.BAD_REQUEST.getCode(), message, null);
    }

    /**
     * 未认证
     *
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> unauthorized() {
        return fail(RStatus.UNAUTHORIZED.getCode(), RStatus.UNAUTHORIZED.getMessage());
    }

    /**
     * 未授权
     *
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> forbidden() {
        return fail(RStatus.FORBIDDEN.getCode(), RStatus.FORBIDDEN.getMessage());
    }

}
