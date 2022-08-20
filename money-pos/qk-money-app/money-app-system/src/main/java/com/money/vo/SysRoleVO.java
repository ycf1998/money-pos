package com.money.vo;

import com.money.entity.SysPermission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "系统角色VO")
public class SysRoleVO implements Serializable {
    private static final long serialVersionUID = 8378480891898934113L;

    private Long id;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "数量")
    private Long count;

    @Schema(description = "角色级别")
    private Integer level;

    @Schema(description = "可用状态：0-禁用；1-启用")
    private Boolean enabled;

    @Schema(description = "角色权限")
    private List<SysPermission> permissions;
}
