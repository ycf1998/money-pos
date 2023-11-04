package com.money.common.context;

import cn.hutool.core.util.StrUtil;
import com.money.common.constant.WebRequestConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;

/**
 * @author : money
 * @version : 1.0.0
 * @description : web请求上下文过滤器
 * @createTime : 2023-03-18 11:40:28
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebRequestContextFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 链路追踪
        String requestId = request.getHeader(WebRequestConstant.HEADER_REQUEST_ID);
        MDC.put("requestId", requestId);
        // 填充请求上下文信息
        this.fillRequestContext(request);
        filterChain.doFilter(request, response);
    }

    /**
     * 填补请求上下文中
     *
     * @param request 请求
     */
    private void fillRequestContext(HttpServletRequest request) {
        WebRequestContext context = new WebRequestContext();
        String requestId = request.getHeader(WebRequestConstant.HEADER_REQUEST_ID);
        String lang = request.getHeader(WebRequestConstant.HEADER_LANG);
        context.setRequestId(requestId);
        context.setLang(lang);
        String timezone = request.getHeader(WebRequestConstant.HEADER_TIMEZONE);
        if (StrUtil.isNotBlank(timezone)) {
            context.setTimezone(ZoneId.of(timezone));
        }
        WebRequestContextHolder.setContext(context);
    }
}
