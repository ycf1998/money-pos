package com.money.common.util;

import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 网络工具
 * @createTime : 2022-01-01 13:33:56
 */
@Slf4j
@UtilityClass
public class WebUtil {

    /**
     * 获取当前请求对象
     *
     * @return {@link HttpServletRequest}
     */
    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getRequest();
    }

    /**
     * 获取当前请求响应对象
     *
     * @return {@link HttpServletResponse}
     */
    public HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getResponse();
    }

    /**
     * json响应
     *
     * @param response   响应
     * @param httpStatus http状态
     * @param o          o
     */
    public void responseJson(HttpServletResponse response, HttpStatus httpStatus, Object o) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(JSONUtil.toJsonStr(o).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("响应返回失败", e);
        }
    }

}
