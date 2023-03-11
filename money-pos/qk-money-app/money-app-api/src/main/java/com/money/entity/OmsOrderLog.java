package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单操作日志
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Getter
@Setter
@TableName("oms_order_log")
@Schema(description = "订单操作日志")
public class OmsOrderLog extends BaseEntity {

    @Schema(description="订单id")
    private Long orderId;

    @Schema(description="描述")
    private String description;

    @Schema(description="租户id")
    private Long tenantId;

}
