package com.money.common.timezone;

import com.money.common.context.WebRequestContextHolder;
import com.money.common.timezone.annotation.TZParam;
import com.money.common.timezone.annotation.TZRep;
import com.money.common.timezone.converter.TimeZoneConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.util.Optional;


/**
 * @author : money
 * @version : 1.0.0
 * @description : 时区切面
 * @createTime : 2022-05-11 22:12:56
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TimeZoneAspect {

    private final TimeZoneProperties timezoneProperties;

    /**
     * 只切带有@TZRep的类或者方法
     */
    @Pointcut("@within(com.money.common.timezone.annotation.TZProcess)")
    public void timezone() {
    }

    @Around(value = "timezone()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ZoneId customTimeZone = WebRequestContextHolder.getContext().getTimezone();
        log.info("客户时区：{}", customTimeZone);
        // 时区为默认时区或者没带时区不处理
        ZoneId defaultZone = ZoneId.of(timezoneProperties.getDefaultTimeZone());
        if (customTimeZone == null || defaultZone.equals(customTimeZone)) {
            return proceedingJoinPoint.proceed();
        }

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 入参时区处理
        Object[] args = proceedingJoinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0, len = args.length; i < len; i++) {
            Object arg = args[i];
            for (Annotation annotation : parameterAnnotations[i]) {
                // 带有注解才进行转换
                if (annotation.annotationType().equals(TZParam.class)) {
                    TZParam tzParam = (TZParam) annotation;
                    String format = tzParam.format();
                    Class<? extends TimeZoneConverter> converter = tzParam.converter();
                    args[i] = converter.newInstance().convert(arg, format, customTimeZone, defaultZone);
                    break;
                }
            }
        }
        long inConsume = System.currentTimeMillis() - startTime;
        log.debug("In parameter time zone convert consume: {} ms", inConsume);

        Object result = proceedingJoinPoint.proceed();
        startTime = System.currentTimeMillis();
        // 出参时区处理
        if (result != null) {
            // 方法上的注解优先
            TZRep tzRep = Optional.ofNullable(method.getAnnotation(TZRep.class))
                    .orElse(proceedingJoinPoint.getTarget().getClass().getAnnotation(TZRep.class));
            if (tzRep != null) {
                // 使用注解上的转换器
                String format = tzRep.format();
                Class<? extends TimeZoneConverter> converter = tzRep.converter();
                result = converter.newInstance().convert(result, format, defaultZone, customTimeZone);
            }
        }
        long outConsume = System.currentTimeMillis() - startTime;
        log.debug("Out parameter time zone convert consume: {} ms", outConsume);
        log.debug("Time zone convert all consume: {} ms", inConsume + outConsume);
        return result;
    }

}