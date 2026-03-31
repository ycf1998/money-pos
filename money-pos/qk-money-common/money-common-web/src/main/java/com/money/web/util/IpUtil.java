package com.money.web.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 获得 IP 的地理位置
     *
     * @param ip ip
     * @return {@link IpAddress}
     */
    public IpAddress getIpAddress(String ip) {
        try {
            Map<String, Object> paramsMap = new HashMap<>(2);
            paramsMap.put("ip", ip);
            paramsMap.put("accessKey", "alibaba-inc");
            String response = HttpUtil.get("https://ip.taobao.com/outGetIpInfo", paramsMap);
            JSONObject resObj = JSON.parseObject(response);
            if (!resObj.isEmpty() && resObj.getIntValue("code") == 0) {
                return resObj.getObject("data", IpAddress.class);
            }
        } catch (Exception e) {
            log.error("获取 IP 地理位置信息失败", e);
        }
        return null;
    }

    /**
     * IP 地址信息
     *
     * @param country 国家
     * @param region  地区
     * @param city    城市
     * @param isp     运营商
     */
    public record IpAddress(
        String country,
        String region,
        String city,
        String isp
    ) {}
}
