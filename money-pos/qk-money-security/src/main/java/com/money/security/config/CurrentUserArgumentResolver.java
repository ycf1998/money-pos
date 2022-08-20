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
 * @author : money
 * @version : 1.0.0
 * @description : 当前用户参数解析器
 * @createTime : 2022-01-07 21:44:44
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> type = methodParameter.getParameter().getType();
        return methodParameter.hasParameterAnnotation(CurrentUser.class)
                && (type.equals(RbacUser.class) || type.equals(String.class) || type.equals(Long.class));
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
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
