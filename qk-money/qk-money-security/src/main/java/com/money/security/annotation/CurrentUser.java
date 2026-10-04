package com.money.security.annotation;

import com.money.security.model.RbacUser;
import com.money.security.config.CurrentUserArgumentResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当前登录用户注解
 * <p>用于 Controller 方法参数上，自动注入当前登录用户信息。</p>
 * <p>支持参数类型：{@link RbacUser}、{@link Long}、{@link String}</p>
 * <p>使用示例：
 * <pre>{@code
 * @GetMapping("/profile")
 * public R<UserVO> getProfile(@CurrentUser RbacUser user) { }
 *
 * @GetMapping("/id")
 * public R<Long> getUserId(@CurrentUser Long userId) { }
 *
 * @GetMapping("/name")
 * public R<String> getUsername(@CurrentUser String username) { }
 * }</pre>
 * </p>
 *
 * @author money
 * @since 1.0.0
 * @see CurrentUserArgumentResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
