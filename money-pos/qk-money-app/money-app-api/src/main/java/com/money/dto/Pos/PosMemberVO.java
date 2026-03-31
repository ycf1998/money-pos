package com.money.dto.Pos;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : money
 * @version : 1.0.0
 * @description : pos成员VO
 * @createTime : 2022-04-14 22:06:22
 */
@Data
public class PosMemberVO {

    private Long id;

    /**
     * 卡号
     */
    private String code;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 会员类型
     */
    private String type;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 抵用券
     */
    private BigDecimal coupon;
}
