package com.money.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不允许为空")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message = "密码不允许为空")
    private String password;

}
