package com.money.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordDTO {

    @Schema(description = "旧密码")
    @NotBlank(message = "旧密码不允许为空")
    private String oldPassword;

    @Schema(description = "新密码")
    @NotBlank(message = "新密码不允许为空")
    private String newPassword;
}
