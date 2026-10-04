package com.money.web.util;

import cn.hutool.extra.spring.SpringUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 使用 DefaultJacksonConfig. class 里定义的 Jackson ObjectMapper 对象
 *
 * @author : money
 * @since : 1.0.0
 */
@UtilityClass
public class JacksonUtil {

    @SneakyThrows
    public String writeAsString(Object o) {
        return SpringUtil.getBean(ObjectMapper.class).writeValueAsString(o);
    }

}
