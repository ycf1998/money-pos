package com.money.dto.OmsOrder;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 返回商品dto
 * @createTime : 2022-04-25 21:07:27
 */
@Data
public class ReturnGoodsDTO {

    /**
     * 订单详情id
     */
    @NotNull
    private Long id;

    /**
     * 数量
     */
    @NotNull
    private Integer quantity;
}
