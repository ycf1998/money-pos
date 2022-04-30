package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author money
 * @since 2022-03-04
 */
@Getter
@Setter
@TableName("sys_role")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色级别")
    private Integer level;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "角色人数")
    private Long count;

    @Schema(description = "可用状态：0-禁用；1-启用")
    private Boolean enabled;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "租户id")
    private Long tenantId;


}
