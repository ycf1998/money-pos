package com.money.web.brand;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BrandVO implements Serializable {
    private static final long serialVersionUID = -7166056262817539491L;

    private Long id;

    /**
     * 品牌logo
     */
    private String logo;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌描述
     */
    private String description;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
