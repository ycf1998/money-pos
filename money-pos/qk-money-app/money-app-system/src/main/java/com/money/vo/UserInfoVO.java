package com.money.vo;

import com.money.entity.SysPermission;
import com.money.entity.SysRole;
import com.money.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "用户信息VO")
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = -1933526574639909374L;
    /**
     * 用户信息
     */
    @Schema(description = "用户信息")
    private SysUser info;
    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<SysRole> roles;
    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private List<SysPermission> permissions;
}
