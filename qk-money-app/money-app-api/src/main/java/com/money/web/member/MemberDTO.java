package com.money.web.member;

import com.money.common.dto.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class MemberDTO {

    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    /**
     * 会员名称
     */
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String name;

    /**
     * 会员类型
     */
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String type;

    /**
     * 联系电话
     */
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
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
     * 备注
     */
    @Size(max = 255, groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String remark;
}
