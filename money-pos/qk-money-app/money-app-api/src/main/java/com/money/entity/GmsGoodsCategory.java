package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author money
 * @since 2022-04-04
 */
@Getter
@Setter
@TableName("gms_goods_category")
public class GmsGoodsCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父分类id
     */
    private Long pid;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 分类名称
     */
    private String name;

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
