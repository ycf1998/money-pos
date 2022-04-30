package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author money
 * @since 2022-04-10
 */
@Getter
@Setter
@TableName("oms_order_detail")
public class OmsOrderDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

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

    private Integer sort;

    /**
     * 租户id
     */
    private Long tenantId;


}
