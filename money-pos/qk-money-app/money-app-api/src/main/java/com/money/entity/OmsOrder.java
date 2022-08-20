package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author money
 * @since 2022-04-10
 */
@Getter
@Setter
@TableName("oms_order")
public class OmsOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 会员名
     */
    private String member;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * vip单
     */
    private Boolean vip;

    /**
     * 状态
     */
    private String status;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 地区
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 总成本
     */
    private BigDecimal costAmount;

    /**
     * 总价
     */
    private BigDecimal totalAmount;

    /**
     * 实付款
     */
    private BigDecimal payAmount;

    /**
     * 抵用券
     */
    private BigDecimal couponAmount;

    /**
     * 最终销售金额
     */
    private BigDecimal finalSalesAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 完成时间
     */
    private LocalDateTime completionTime;

    private Integer sort;

    /**
     * 租户id
     */
    private Long tenantId;


}
