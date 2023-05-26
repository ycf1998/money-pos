package com.money.dto;

import com.money.common.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SysTenantDTO {

    @Schema(description = "租户id")
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description = "租户code")
    @NotBlank(message = "租户code不允许为空", groups = {ValidGroup.Save.class})
    private String tenantCode;

    @Schema(description = "ico")
    private String ico;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "租户名称")
    @NotBlank(message = "租户名称不允许为空", groups = {ValidGroup.Save.class})
    private String tenantName;

    @Schema(description = "租户描述")
    @Size(max = 255, message = "租户描述最长不允许超过255个字符", groups = {ValidGroup.Save.class})
    private String tenantDesc;

}
