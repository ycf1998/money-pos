package com.money.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色资源权限关联表
 * </p>
 *
 * @author money
 * @since 2022-03-04
 */
@Getter
@Setter
@TableName("sys_role_permission_relation")
public class SysRolePermissionRelation  {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "资源权限id")
    private Long permissionId;

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "租户id")
    private Long tenantId;


}
