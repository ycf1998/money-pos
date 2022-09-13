package com.money.dto.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 订单细节VO
 * @createTime : 2022-04-23 18:22:31
 */
@Data
public class OrderDetailVO {

    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 状态
     */
    private String status;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品条码
     */
    private String goodsBarcode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 实际单价
     */
    private BigDecimal goodsPrice;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 售价
     */
    private BigDecimal salePrice;

    /**
     * 进价
     */
    private BigDecimal purchasePrice;

    /**
     * 会员价
     */
    private BigDecimal vipPrice;

    /**
     * 抵用券
     */
    private BigDecimal coupon;

    /**
     * 退货数量
     */
    private Integer returnQuantity;
}
