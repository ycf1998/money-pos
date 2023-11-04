package com.money.common.response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 忽视全球反应
 * @createTime : 2022-01-01 13:26:38
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreGlobalResponse {
}
