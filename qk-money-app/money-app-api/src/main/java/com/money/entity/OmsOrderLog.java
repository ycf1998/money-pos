package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单操作日志
 * </p>
 *
 * @author money
 * @since 2022-04-26
 */
@Getter
@Setter
@TableName("oms_order_log")
public class OmsOrderLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 描述
     */
    private String description;

    private Integer sort;

    /**
     * 租户id
     */
    private Long tenantId;


}
