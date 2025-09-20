package com.money.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记当前登录用户信息
 * <pre>
 * 在 Controller 方法参数上使用此注解，可自动注入当前登录用户信息
 * 参数类型为 {@link com.money.security.model.RbacUser} 时注入 RbacUser
 * 参数类型为 Long 时注入用户ID
 * 参数类型为 String 时注入用户名
 * </pre>
 *
 * @author : money
 * @since : 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
