package com.money.web.log;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.money.web.context.WebRequestContextHolder;
import com.money.web.util.IpUtil;
import com.money.web.util.JacksonUtil;
import com.money.web.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 默认 Web 日志切面
 *
 * @author : money
 * @since : 1.0.0
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class DefaultWebLogAspect {

    private final WebLogAspectProperties properties;

    @Around("execution(public * com.money..controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime now = LocalDateTime.now();
        long startTime = Instant.now().toEpochMilli();
        log.info("====================");

        HttpServletRequest request = WebUtil.getRequest();
        String ip = IpUtil.getIp(request);
        String requestMethod = request.getMethod();
        String url = request.getRequestURI() + (StrUtil.isNotBlank(request.getQueryString()) ? "?" + request.getQueryString() : "");
        log.info("{} {}", requestMethod, url);
        log.info("{}", WebRequestContextHolder.getContext());
        log.info("> {}", ip);

        WebLogAspectProperties.Mode mode = properties.getMode();
        boolean logBody = mode != WebLogAspectProperties.Mode.SIMPLE;
        boolean logResult = mode != WebLogAspectProperties.Mode.SIMPLE && !(mode == WebLogAspectProperties.Mode.IGNORE_GET_RESULT && "GET".equalsIgnoreCase(requestMethod));

        String body = "";
        if (logBody) {
            body = Opt.ofBlankAble(resolveBody(request)).orElse("-");
            log.info("body: {}", body);
        }


        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long spendTime = endTime - startTime;
            if (logResult) {
                log.info("result: {}", JacksonUtil.writeAsString(result));
            }
            log.info("spend time: {}ms", spendTime);
            // TODO 记录日志
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
            } else if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrappedRequest = (ContentCachingRequestWrapper) request;
                byte[] contentAsByteArray = wrappedRequest.getContentAsByteArray();
                return new String(contentAsByteArray, StandardCharsets.UTF_8);
            } else {
                return StrUtil.EMPTY;
            }
        } catch (Exception e) {
            log.error("Request body resolve fail", e);
            return "resolve fail";
        }
    }

}