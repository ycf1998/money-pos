package com.money.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.money.common.dto.PageRequest;
import com.money.common.util.BeanMapUtil;
import com.money.common.vo.PageVO;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 分页工具
 * @createTime : 2022-04-24 19:46:53
 */
@UtilityClass
public class PageUtil {

    public <T> Page<T> toPage(PageRequest pageRequest) {
        return new Page<>(pageRequest.getPage(), pageRequest.getSize());
    }

    public static <T> PageVO<T> toPageVO(IPage<T> page) {
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), page.getRecords());
    }

    public static <T, R> PageVO<R> toPageVO(IPage<T> page, Supplier<R> supplier) {
        List<R> records = BeanMapUtil.to(page.getRecords(), supplier);
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), records);
    }

    public static <T, R> PageVO<R> toPageVO(IPage<T> page, Function<? super T, R> map) {
        List<R> records = page.getRecords().stream().map(map).collect(Collectors.toList());
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), records);
    }
}
