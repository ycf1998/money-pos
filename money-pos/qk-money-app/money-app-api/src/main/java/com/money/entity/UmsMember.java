package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author money
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("ums_member")
public class UmsMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

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

    /**
     * 逻辑删除
     */
    private Boolean deleted;

    private Integer sort;

    /**
     * 租户id
     */
    private Long tenantId;


}
