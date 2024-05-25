package com.money.common.log;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.money.common.context.WebRequestContextHolder;
import com.money.common.util.IpUtil;
import com.money.common.util.JacksonUtil;
import com.money.common.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

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
        String url = request.getRequestURI() + (StrUtil.isNotBlank(request.getQueryString()) ? "?" + request.getQueryString() : "");
        String body = resolveBody(request);
        String ip = IpUtil.getIp(request);
        log.info("> {}", ip);
        log.info("{} {}", requestMethod, url);
        log.info("{}", WebRequestContextHolder.getContext());
        if (StrUtil.isNotBlank(body)) {
            log.info("body: {}", body);
        }

        // 执行
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long spendTime = endTime - startTime;
            WebLog.builder()
                    .requestId(WebRequestContextHolder.getContext().getRequestId())
                    .requestTime(now)
                    .ip(ip)
                    .method(requestMethod)
                    .url(url)
                    .queryString(request.getQueryString())
                    .uri(request.getRequestURI())
                    .body(body)
                    .result(result)
                    .spendTime(spendTime)
                    .build();
            log.info("result: {}", JacksonUtil.writeAsString(result));
            log.info("spend time: {}ms", spendTime);
        }
        return result;
    }

    /**
     * 解析正文
     *
     * @param request 请求
     * @return {@link String}
     */
    private String resolveBody(HttpServletRequest request) {
        try {
            String contentType = request.getContentType();
            if (StrUtil.startWithIgnoreCase(contentType, ContentType.MULTIPART.getValue())) {
                StringBuilder sb = new StringBuilder();
                for (Part part : request.getParts()) {
                    String partContentType = part.getContentType();
                    String name = part.getName();
                    if (partContentType == null || StrUtil.startWithIgnoreCase(partContentType, ContentType.JSON.getValue())) {
                        sb.append(name).append("=").append(((ApplicationPart) part).getString("UTF-8")).append("&");
                    } else {
                        sb.append(name).append("=")
                                .append(part.getSubmittedFileName())
                                .append("|").append(partContentType)
                                .append("|").append(FileUtil.readableFileSize(part.getSize()))
                                .append("&");
                    }
                }
                return sb.deleteCharAt(sb.length() - 1).toString();
            } else if (StrUtil.startWithIgnoreCase(contentType, ContentType.FORM_URLENCODED.getValue())) {
                StringBuilder sb = new StringBuilder();
                request.getParameterMap().forEach((k, v) -> sb.append(k).append("=").append(Arrays.toString(v)).append("&"));
                return sb.deleteCharAt(sb.length() - 1).toString();
            } else {
                return IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("Request body resolve fail", e);
            return "resolve fail";
        }
    }
}