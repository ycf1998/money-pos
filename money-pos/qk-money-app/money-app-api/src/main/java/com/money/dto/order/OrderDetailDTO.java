package com.money.dto.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 订单详情dto
 * @createTime : 2022-04-20 00:01:14
 */
@Data
public class OrderDetailDTO {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品条码
     */
    private String goodsBarcode;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 实际单价
     */
    private BigDecimal goodsPrice;

    /**
     * 售价
     */
    private BigDecimal salePrice;

    /**
     * 会员价
     */
    private BigDecimal vipPrice;

    /**
     * 抵用券
     */
    private BigDecimal coupon;
}
