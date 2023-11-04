package com.money.dto;

import lombok.Data;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 下拉框VO
 * @createTime : 2022-04-03 16:24:37
 */
@Data
public class SelectVO {

    /**
     * 标签
     */
    private String label;

    /**
     * 值
     */
    private Object value;
}
