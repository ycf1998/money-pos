package com.money.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class IpUtil {

    /**
     * 获得Ip
     *
     * @param request 请求
     * @return {@link String}
     */
    public String getIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获得ip真实地址
     *
     * @param ip 知识产权
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
            log.error("获取IP真实地址信息失败", e);
        }
        return null;
    }

    @Data
    public static class IpAddress {
        /**
         * 国家
         */
        private String country;
        /**
         * 地区
         */
        private String region;
        /**
         * 城市
         */
        private String city;
        /**
         * 运营商
         */
        private String isp;
    }
}
