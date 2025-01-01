package com.money.dto.UmsMember;

import com.money.web.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
* <p>
* 会员表
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@Schema(description = "会员表")
public class UmsMemberDTO {

    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description="会员名称")
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String name;

    @Schema(description="会员类型")
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String type;

    @Schema(description="手机号")
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String phone;

    @Schema(description="省份")
    private String province;

    @Schema(description="城市")
    private String city;

    @Schema(description="地区")
    private String district;

    @Schema(description="详细地址")
    private String address;

    @Schema(description="抵用券")
    private BigDecimal coupon;

    @Schema(description="备注")
    @Size(max = 255, groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String remark;

}
