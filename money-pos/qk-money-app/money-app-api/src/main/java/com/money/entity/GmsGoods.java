package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author money
 * @since 2022-04-04
 */
@Getter
@Setter
@TableName("gms_goods")
public class GmsGoods extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 标签
     */
    private String label;

    /**
     * 条码
     */
    private String barcode;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品拼音
     */
    private String pinyin;

    /**
     * 商品图片
     */
    private String pic;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String size;

    /**
     * 描述
     */
    private String description;

    /**
     * 进价
     */
    private BigDecimal purchasePrice;

    /**
     * 售价
     */
    private BigDecimal salePrice;

    /**
     * 会员价
     */
    private BigDecimal vipPrice;

    /**
     * 用券
     */
    private BigDecimal coupon;

    /**
     * 库存
     */
    private Long stock;

    /**
     * 销量
     */
    private Long sales;

    /**
     * 状态
     */
    private String status;

    private Integer sort;

    /**
     * 租户id
     */
    private Long tenantId;


}
