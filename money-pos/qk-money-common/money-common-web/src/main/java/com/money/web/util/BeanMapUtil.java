package com.money.web.util;

import cn.hutool.core.bean.BeanUtil;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 对象转换工具
 *
 * @author : money
 * @since : 1.0.0
 */
@UtilityClass
public class BeanMapUtil {

    public <T, R> R to(T t, Supplier<R> supplier) {
        R r = supplier.get();
        BeanUtil.copyProperties(t, r);
        return r;
    }

    public <T, R> List<R> to(Collection<T> tList, Supplier<R> supplier) {
        return tList.stream().map(e -> BeanMapUtil.to(e, supplier)).collect(Collectors.toList());
    }

}
