package com.money.dto.member;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MemberVO implements Serializable {

    private static final long serialVersionUID = 6154484576837821473L;
    
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
     * 联系电话
     */
    private String phone;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 抵用券
     */
    private BigDecimal coupon;

    /**
     * 总消费金额
     */
    private BigDecimal consumeAmount;

    /**
     * 消费抵用券
     */
    private BigDecimal consumeCoupon;

    /**
     * 消费次数
     */
    private Integer consumeTimes;

    /**
     * 取消次数
     */
    private Integer cancelTimes;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
