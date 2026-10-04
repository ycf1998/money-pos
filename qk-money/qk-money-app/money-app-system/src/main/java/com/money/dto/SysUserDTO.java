package com.money.dto;

import com.money.web.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.util.Set;

@Data
public class SysUserDTO {

    @Schema(description = "用户 ID，更新时必填")
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description = "用户登录名，全局唯一，注册后不可修改")
    @NotBlank(message = "用户名不允许为空", groups = ValidGroup.Save.class)
    private String username;

    @Schema(description = "用户昵称，可修改")
    @NotBlank(message = "昵称不允许为空", groups = {ValidGroup.Save.class})
    private String nickname;

    @Schema(description = "头像 URL")
    private String avatar;

    @Schema(description = "手机号，用于接收验证码")
    @NotBlank(message = "手机号不允许为空", groups = {ValidGroup.Save.class})
    private String phone;

    @Schema(description = "邮箱地址")
    @Email(message = "邮箱格式不正确", groups = {ValidGroup.Save.class})
    private String email;

    @Schema(description = "是否启用，true 表示可用")
    @NotNull(groups = {ValidGroup.Save.class})
    private Boolean enabled;

    @Schema(description = "备注信息，最长 255 字符")
    @Size(max = 255, message = "备注最长不允许超过 255 个字符", groups = {ValidGroup.Save.class})
    private String remark;

    @Schema(description = "角色 ID 集合，用于分配用户角色")
    @NotEmpty(message = "角色不能为空", groups = {ValidGroup.Save.class})
    private Set<Long> roles;
}
