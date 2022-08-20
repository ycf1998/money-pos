package com.money.util;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.money.common.vo.PageVO;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : money
 * @version : 1.0.0
 * @description : VO工具类
 * @createTime : 2022-04-14 22:14:59
 */
@UtilityClass
public class VOUtil {

    public <T, R> R toVO(T t, Class<R> voClass) {
        R vo;
        try {
            vo = voClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        BeanUtil.copyProperties(t, vo);
        return vo;
    }

    public <T, R> List<R> toVO(List<T> tList, Class<R> voClass) {
        return tList.stream().map(record -> {
            R vo;
            try {
                vo = voClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            BeanUtil.copyProperties(record, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public static <T> PageVO<T> toPageVO(IPage<T> page) {
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), page.getRecords());
    }

    public static <T, R> PageVO<R> toPageVO(IPage<T> page, Class<R> voClass) {
        List<R> records = VOUtil.toVO(page.getRecords(), voClass);
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), records);
    }

    public static <T, R> PageVO<R> toPageVO(IPage<T> page, Function<? super T, R> map) {
        List<R> records = page.getRecords().stream().map(map).collect(Collectors.toList());
        return new PageVO<>(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), records);
    }
}
