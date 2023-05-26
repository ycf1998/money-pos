package com.money.dto;

import com.money.common.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Set;

@Data
public class SysUserDTO {

    @Schema(description = "用户id")
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不允许为空", groups = ValidGroup.Save.class)
    private String username;

    @Schema(description = "昵称")
    @NotBlank(message = "昵称不允许为空", groups = {ValidGroup.Save.class})
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不允许为空", groups = {ValidGroup.Save.class})
    private String phone;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确", groups = {ValidGroup.Save.class})
    private String email;

    @Schema(description = "可用状态")
    @NotNull(groups = {ValidGroup.Save.class})
    private Boolean enabled;

    @Schema(description = "备注")
    @Size(max = 255, message = "备注最长不允许超过255个字符", groups = {ValidGroup.Save.class})
    private String remark;

    @Schema(description = "角色id")
    @NotEmpty(message = "角色不能为空", groups = {ValidGroup.Save.class})
    private Set<Long> roles;
}
