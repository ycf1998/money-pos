package com.money.common.util;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 使用 DefaultJacksonConfig.class 里定义的 Jackson ObjectMapper 对象
 * @createTime : 2023-06-18 12:08:56
 */
@UtilityClass
public class DefaultJackson {

    @SneakyThrows
    public String writeAsString(Object o) {
        return SpringUtil.getBean(ObjectMapper.class).writeValueAsString(o);
    }

}
