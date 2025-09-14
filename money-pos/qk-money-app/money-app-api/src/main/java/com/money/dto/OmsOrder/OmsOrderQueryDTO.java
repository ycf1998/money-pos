package com.money.dto.OmsOrder;

import com.money.web.dto.PageQueryRequest;
import com.money.web.util.MoneyCommUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
* <p>
* 订单表
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "订单表")
public class OmsOrderQueryDTO extends PageQueryRequest {

    @Schema(description="订单号")
    private String orderNo;

    @Schema(description="会员名")
    private String member;

    @Schema(description="状态")
    private String status;

    @Schema(description="开始时间")
    private LocalDateTime startTime;

    @Schema(description="结束时间")
    private LocalDateTime endTime;

    @Override
    public Map<String, String> sortKeyMap() {
        return MoneyCommUtil.sortFieldMap("createTime", "paymentTime", "completionTime");
    }
}
