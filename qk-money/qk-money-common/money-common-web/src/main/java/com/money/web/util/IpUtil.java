package com.money.web.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * IP 工具
 *
 * @author : money
 * @since : 1.0.0
 */
@Slf4j
@UtilityClass
public class IpUtil {

    private static final String[] IP_HEADERS = {
        "x-forwarded-for",
        "Proxy-Client-IP",
        "X-Forwarded-For",
        "WL-Proxy-Client-IP",
        "X-Real-IP"
    };

    /**
     * 获取 IP
     *
     * @param request 请求
     * @return {@link String}
     */
    public String getIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = Arrays.stream(IP_HEADERS)
            .map(request::getHeader)
            .filter(ipStr -> StrUtil.isNotBlank(ipStr) && !"unknown".equalsIgnoreCase(ipStr))
            .findFirst()
            .orElse(request.getRemoteAddr());

        // 处理多级代理的情况，取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // IPv6 本地回环地址转换为 IPv4
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }

}
