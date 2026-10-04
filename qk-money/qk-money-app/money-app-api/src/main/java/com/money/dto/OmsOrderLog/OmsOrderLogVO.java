package com.money.dto.OmsOrderLog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
* <p>
* 订单操作日志
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@Schema(description = "订单操作日志")
public class OmsOrderLogVO {

    private Long id;

    @Schema(description="订单id")
    private Long orderId;

    @Schema(description="描述")
    private String description;

    @Schema(description="创建人")
    private String createBy;

    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
