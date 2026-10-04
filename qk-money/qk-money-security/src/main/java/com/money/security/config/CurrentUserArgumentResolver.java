package com.money.security.config;

import com.money.security.SecurityGuard;
import com.money.security.annotation.CurrentUser;
import com.money.security.model.RbacUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 当前用户参数解析器
 * <p>解析 {@link CurrentUser} 注解，根据参数类型注入对应的用户信息。</p>
 * <p>支持类型：
 * <ul>
 *     <li>{@link RbacUser}：注入完整用户信息</li>
 *     <li>{@link Long}：注入用户 ID</li>
 *     <li>{@link String}：注入用户名</li>
 * </ul>
 * </p>
 *
 * @author money
 * @since 1.0.0
 * @see CurrentUser
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 是否支持该参数类型
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> type = methodParameter.getParameter().getType();
        return methodParameter.hasParameterAnnotation(CurrentUser.class)
                && (type.equals(RbacUser.class) || type.equals(String.class) || type.equals(Long.class));
    }

    /**
     * 解析参数值
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {
        Class<?> type = methodParameter.getParameter().getType();
        RbacUser rbacUser = SecurityGuard.getRbacUser();
        if (type.equals(RbacUser.class)) {
            return rbacUser;
        } else if (type.equals(Long.class)) {
            return rbacUser.getUserId();
        } else {
            return rbacUser.getUsername();
        }
    }
}
