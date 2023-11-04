package com.money.dto.Home;

import com.money.dto.OmsOrder.OrderCountVO;
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
    OrderCountVO total;

    /**
     * 库存价值
     */
    private BigDecimal inventoryValue;

}