package com.money.dto.OmsOrder;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 订单统计VO
 * @createTime : 2022-01-31 17:54:28
 */
@Data
public class OrderCountVO {

    /**
     * 订单数
     */
    private Long orderCount;

    /**
     * 销售额
     */
    private BigDecimal saleCount;

    /**
     * 成本
     */
    private BigDecimal costCount;

    /**
     * 利润
     */
    private BigDecimal profit;
}
