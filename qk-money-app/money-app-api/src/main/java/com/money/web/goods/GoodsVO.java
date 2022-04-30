package com.money.web.goods;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 商品VO
 * @createTime : 2022-04-04 19:08:16
 */
@Data
public class GoodsVO {

    private Long id;

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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
