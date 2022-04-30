package com.money.common.log;


import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 默认全局web日志方面
 * @createTime : 2022-01-01 13:26:12
 */
@Slf4j
@Aspect
@Order(-1)
@Component
@ConditionalOnProperty(prefix = "money.web", name = "web-log-aspect", matchIfMissing = true)
public class DefaultWebLogAspect {

    @Pointcut("execution(public * com.money..controller..*.*(..))")
    public void webLog() {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime now = LocalDateTime.now();
        long startTime = Instant.now().toEpochMilli();
        log.info("=============================================");
        // 获取当前请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 记录请求信息(通过Logstash传入Elasticsearch)
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String requestMethod = request.getMethod();
        String url = request.getRequestURL().toString();
        log.info("{} {}", requestMethod, url);
        // swagger注解描述
        String description = "";
//        if (method.isAnnotationPresent(Operation.class)) {
//            Operation operation = method.getAnnotation(Operation.class);
//            description = operation.summary();
//            log.info("描述：{}", description);
//        }
        // 执行
        Object result = joinPoint.proceed();
        long endTime = Instant.now().toEpochMilli();
        long spendTime = endTime - startTime;
        WebLog webLog = WebLog.builder()
                .basePath(url.replace(new URL(url).getPath(), ""))
                .description(description)
                .username(request.getRemoteUser())
                .ip(request.getRemoteAddr())
                .method(requestMethod)
                .parameter(getParameter(method, joinPoint.getArgs()))
                .url(url)
                .uri(request.getRequestURI())
                .result(result)
                .operationTime(now.toString())
                .spendTime(spendTime)
                .build();
        log.info("detail {}", JSONUtil.toJsonStr(webLog));
        log.info("spend time: {}ms", spendTime);
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
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String key = requestParam.value().equals("") ? parameters[i].getName() : requestParam.value();
                argList.add(Collections.singletonMap(key, args[i]));
            } else {
                argList.add(Collections.singletonMap(parameters[i].getName(), args[i]));
            }
        }
        return argList.size() > 0 ? argList.get(0) : argList;
    }

}