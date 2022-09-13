package com.money.dto.order;

import com.money.common.dto.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 订单查询dto
 * @createTime : 2022-04-10 11:18:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderQueryDTO extends QueryRequest {
    private static final long serialVersionUID = 6840559592721306293L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 会员
     */
    private String member;

    /**
     * 状态
     */
    private String status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
