package com.money.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : money
 * @version : 1.0.0
 * @description :
 * 在controller的方法入参上添加该注解，则注入当前登录人信息
 * 参数类型为 {@link com.money.security.model.RbacUser} 注入RbacUser
 * 参数类型为 Long 注入用户id
 * 参数类型为 String 注入用户名
 * @createTime : 2022-03-26 11:47:33
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
