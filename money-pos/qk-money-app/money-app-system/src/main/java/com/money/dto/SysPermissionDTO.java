package com.money.dto;

import com.money.common.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysPermissionDTO {

    @Schema(description = "权限id")
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "权限名称")
    @NotBlank(message = "权限名称不允许为空", groups = {ValidGroup.Save.class})
    private String permissionName;

    @Schema(description = "资源类型")
    @NotBlank(message = "权限类型不允许为空", groups = {ValidGroup.Save.class})
    private String permissionType;

    @Schema(description = "父编码")
    @NotNull(message = "父编码不允许为空", groups = {ValidGroup.Save.class})
    private Long parentId;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "路由地址")
    private String routerPath;

    @Schema(description = "是否外链菜单")
    @NotNull(groups = {ValidGroup.Save.class})
    private Boolean iframe;

    @Schema(description = "是否隐藏")
    @NotNull(groups = {ValidGroup.Save.class})
    private Boolean hidden;

    @Schema(description = "组件名称")
    private String componentName;

    @Schema(description = "组件路径")
    private String componentPath;

    @Schema(description = "排序")
    @NotNull(groups = {ValidGroup.Save.class})
    private Integer sort;
}
