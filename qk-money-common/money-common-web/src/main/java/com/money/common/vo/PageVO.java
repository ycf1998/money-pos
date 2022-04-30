package com.money.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 分面VO
 * @createTime : 2022-03-10 23:20:50
 */
@Data
@AllArgsConstructor
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1894806388550035718L;

    private long current;

    private long size;

    private long pages;

    private long total;

    private List<T> records;

}
