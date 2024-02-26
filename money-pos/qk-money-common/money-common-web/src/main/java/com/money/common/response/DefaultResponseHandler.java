package com.money.common.response;


import com.money.common.util.JacksonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 默认全局响应处理器
 * @createTime : 2022-01-01 13:26:30
 */
@RestControllerAdvice(basePackages = "com.money")
@ConditionalOnProperty(prefix = "money.web", name = "response-handler", matchIfMissing = true)
public class DefaultResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.getDeclaringClass().getAnnotation(IgnoreGlobalResponse.class) == null
                && !methodParameter.hasMethodAnnotation(IgnoreGlobalResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 当已经是 R 类型就无需处理
        if (o instanceof R) {
            return o;
        }
        // 当返回类型是 String 时，用的是 StringHttpMessageConverter 转换器，无法转换为 JSON 格式
        if (o instanceof String) {
            return JacksonUtil.writeAsString(R.success(o));
        }
        return R.success(o);
    }
}
