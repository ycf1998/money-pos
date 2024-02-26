package com.money.common.log;


import com.money.common.constant.ProjectConstants;
import com.money.common.context.WebRequestContextHolder;
import com.money.common.util.IpUtil;
import com.money.common.util.JacksonUtil;
import com.money.common.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 默认全局 Web 日志方面
 * @createTime : 2022-01-01 13:26:12
 */
@Slf4j
@Aspect
@Order(-1)
@Component
@ConditionalOnProperty(prefix = "money.web", name = "web-log-aspect", matchIfMissing = true)
public class DefaultWebLogAspect {

    @Around("execution(public * com.money..controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime now = LocalDateTime.now();
        long startTime = Instant.now().toEpochMilli();
        log.info("=============================================");

        HttpServletRequest request = WebUtil.getRequest();
        String requestMethod = request.getMethod();
        String url = request.getRequestURL().toString();
        log.info("{} {}", requestMethod, url);
        log.info("{}", WebRequestContextHolder.getContext());

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        // 执行
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long spendTime = endTime - startTime;
            WebLog webLog = WebLog.builder()
                    .requestId(WebRequestContextHolder.getContext().getRequestId())
                    .requestTime(now)
                    .ip(IpUtil.getIp(request))
                    .method(requestMethod)
                    .url(url)
                    .uri(request.getRequestURI())
                    .parameter(this.getParameter(method, joinPoint.getArgs()))
                    .result(result)
                    .spendTime(spendTime)
                    .build();
            log.info("detail {}", JacksonUtil.writeAsString(webLog));
            log.info("spend time: {}ms", spendTime);
        }
        return result;
    }


    /**
     * 得到参数
     *
     * @param method 方法
     * @param args   arg
     * @return {@link Object}
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String typeName = parameter.getParameterizedType().getTypeName();
            // 仅记录 JDK 和用户包下的参数
            if (!typeName.startsWith("java.") && !typeName.startsWith(ProjectConstants.PACKAGE)) {
                continue;
            }
            // 将 RequestParam 注解修饰的参数作为请求参数
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String key = requestParam.value().isEmpty() ? parameter.getName() : requestParam.value();
                argList.add(Collections.singletonMap(key, args[i]));
            } else {
                argList.add(Collections.singletonMap(parameter.getName(), args[i]));
            }
        }
        return argList.size() == 1 ? argList.get(0) : argList;
    }

}