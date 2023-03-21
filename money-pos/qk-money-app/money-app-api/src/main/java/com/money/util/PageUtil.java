package com.money.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.money.common.dto.PageRequest;
import lombok.experimental.UtilityClass;

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
}
