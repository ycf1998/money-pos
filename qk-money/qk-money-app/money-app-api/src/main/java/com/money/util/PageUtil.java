package com.money.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.money.web.dto.PageRequest;
import com.money.web.util.BeanMapUtil;
import com.money.web.vo.PageVO;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 分页工具
 *
 * @author : money
 * @since : 1.0.0
 */
@UtilityClass
public class PageUtil {

    /**
     * 转为 Mybatis-plus 分页查询参数
     *
     * @param pageRequest 页面请求
     * @return {@link Page }<{@link T }>
     */
    public <T> Page<T> toPage(PageRequest pageRequest) {
        return new Page<>(pageRequest.getPage(), pageRequest.getSize());
    }

    /**
     * 内存分页
     *
     * @param allRecords  所有记录
     * @param pageRequest 分页请求
     * @return {@link PageVO }<{@link T }>
     */
    public <T> PageVO<T> toPageVO(Collection<T> allRecords, PageRequest pageRequest) {
        List<T> records = allRecords.stream().skip((pageRequest.getPage() - 1) * pageRequest.getSize())
                .limit(pageRequest.getSize()).collect(Collectors.toList());
        return new PageVO<>(pageRequest.getPage(), pageRequest.getSize(), allRecords.size(), records);
    }

    /**
     * 转为分页VO
     *
     * @param page 分页
     * @return {@link PageVO }<{@link T }>
     */
    public <T> PageVO<T> toPageVO(IPage<T> page) {
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    /**
     * 转为其他对象的分页 VO
     *
     * @param page     分页
     * @param supplier 目标对象
     * @return {@link PageVO }<{@link R }>
     */
    public <T, R> PageVO<R> toPageVO(IPage<T> page, Supplier<R> supplier) {
        List<R> records = BeanMapUtil.to(page.getRecords(), supplier);
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getTotal(), records);
    }

    /**
     * 转为其他对象的分页 VO
     *
     * @param page 分页
     * @param map  自定义转换函数
     * @return {@link PageVO }<{@link R }>
     */
    public static <T, R> PageVO<R> toPageVO(IPage<T> page, Function<? super T, R> map) {
        List<R> records = page.getRecords().stream().map(map).collect(Collectors.toList());
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getTotal(), records);
    }
}
