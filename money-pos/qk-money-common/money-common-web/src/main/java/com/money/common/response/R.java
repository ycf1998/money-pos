package com.money.common.response;


import com.money.common.i18n.I18nSupport;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class R<T> implements Serializable {

    private static final long serialVersionUID = -9068318251197180009L;

    @Schema(description = "返回码")
    private int code;

    @Schema(description = "返回信息")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    public R() {
    }

    public R(int code, String message, T data) {
        this.code = code;
        this.message = I18nSupport.get(message);
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @return {@link R}<{@link String}>
     */
    public static <T> R<T> success() {
        return new R<>(RStatus.SUCCESS.getCode(), RStatus.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 数据
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> success(T data) {
        return new R<>(RStatus.SUCCESS.getCode(), RStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param message 消息
     * @param data    数据
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> success(String message, T data) {
        return new R<>(RStatus.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> fail() {
        return new R<>(RStatus.FAILED.getCode(), RStatus.FAILED.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param message 消息
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> fail(String message) {
        return new R<>(RStatus.FAILED.getCode(), message, null);
    }

    /**
     * 失败
     * 失败返回结果
     *
     * @param code    代码
     * @param message 消息
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    /**
     * 参数验证失败返回结果
     *
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> validateFailed() {
        return fail(RStatus.BAD_REQUEST.getCode(), RStatus.BAD_REQUEST.getMessage());
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 消息
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> validateFailed(String message) {
        return new R<>(RStatus.BAD_REQUEST.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     *
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> unauthorized() {
        return fail(RStatus.UNAUTHORIZED.getCode(), RStatus.UNAUTHORIZED.getMessage());
    }

    /**
     * 未登录返回结果
     *
     * @param data 数据
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> unauthorized(T data) {
        return new R<>(RStatus.UNAUTHORIZED.getCode(), RStatus.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     *
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> forbidden() {
        return fail(RStatus.FORBIDDEN.getCode(), RStatus.FORBIDDEN.getMessage());
    }

    /**
     * 未授权返回结果
     *
     * @param data 数据
     * @return {@link R}<{@link T}>
     */
    public static <T> R<T> forbidden(T data) {
        return new R<>(RStatus.FORBIDDEN.getCode(), RStatus.FORBIDDEN.getMessage(), data);
    }

}
