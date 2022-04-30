package com.money.constant;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 订单状态
 * @createTime : 2022-04-20 22:05:34
 */
public enum OrderStatusEnum {

    /**
     * 已确认
     */
    CONFIRMED,
    /**
     * 已支付
     */
    PAID,
    /**
     * 已完成
     */
    DONE,
    /**
     * 退单
     */
    RETURN;
}
