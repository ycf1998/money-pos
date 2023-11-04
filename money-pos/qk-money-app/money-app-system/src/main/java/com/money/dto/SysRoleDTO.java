package com.money.dto;

import com.money.common.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SysRoleDTO {

    @Schema(description = "角色id")
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description = "角色编码")
    @NotBlank(message = "角色编码不允许为空", groups = ValidGroup.Save.class)
    private String roleCode;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不允许为空", groups = {ValidGroup.Save.class})
    private String roleName;

    @Schema(description = "角色级别")
    @NotNull(message = "角色级别不允许为空", groups = {ValidGroup.Save.class})
    @Min(value = 1, groups = {ValidGroup.Save.class})
    private Integer level;

    @Schema(description = "描述")
    @Size(max = 255, message = "描述最长不允许超过255个字符", groups = {ValidGroup.Save.class})
    private String description;

    @Schema(description = "可用状态")
    @NotNull(message = "可用状态不允许为空", groups = {ValidGroup.Save.class})
    private Boolean enabled;

}
