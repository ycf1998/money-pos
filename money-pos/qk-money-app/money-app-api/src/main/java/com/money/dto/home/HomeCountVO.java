package com.money.dto.home;

import com.money.dto.order.OrderCountVO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HomeCountVO {

    /**
     * 今天
     */
    OrderCountVO today;

    /**
     * 月
     */
    OrderCountVO month;

    /**
     * 一年
     */
    OrderCountVO year;

    /**
     * 所有
     */
    OrderCountVO all;

    /**
     * 库存价值
     */
    private BigDecimal inventoryValue;

}