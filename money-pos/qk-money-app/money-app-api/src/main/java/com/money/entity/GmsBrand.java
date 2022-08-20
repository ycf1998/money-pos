package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品品牌表
 * </p>
 *
 * @author money
 * @since 2022-04-04
 */
@Getter
@Setter
@TableName("gms_brand")
public class GmsBrand extends BaseEntity {

    private static final long serialVersionUID = 1L;

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

    private Integer sort;

    /**
     * 租户id
     */
    private Long tenantId;


}
