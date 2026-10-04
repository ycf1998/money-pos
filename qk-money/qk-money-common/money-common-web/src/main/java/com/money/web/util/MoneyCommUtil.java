package com.money.web.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class MoneyCommUtil {

    /**
     * 排序字段映射(驼峰转下划线)
     *
     * @param params 参数
     * @return {@link Map }<{@link String }, {@link String }>
     */
    public Map<String, String> sortFieldMap(String ...params) {
        if (params == null || params.length == 0) {
            return Collections.emptyMap();
        }
        return Arrays.stream(params).collect(Collectors.toMap(param -> param, StrUtil::toUnderlineCase));
    }

}
