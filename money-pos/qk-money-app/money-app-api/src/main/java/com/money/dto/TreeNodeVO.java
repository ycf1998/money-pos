package com.money.dto;

import lombok.Data;

import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 树节点VO
 * @createTime : 2022-04-05 20:23:27
 */
@Data
public class TreeNodeVO {

    private Long id;

    private Long pid;

    private String name;

    private String icon;

    private List<TreeNodeVO> children;
}
