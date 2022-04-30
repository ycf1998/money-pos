package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 资源权限表
 * </p>
 *
 * @author money
 * @since 2022-03-04
 */
@Getter
@Setter
@TableName("sys_permission")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    private String permissionName;

    @Schema(description = "资源类型")
    private String permissionType;

    @Schema(description = "父编码")
    private Long parentId;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "路由地址")
    private String routerPath;

    @Schema(description = "是否外链菜单")
    private Boolean iframe;

    @Schema(description = "是否隐藏")
    private Boolean hidden;

    @Schema(description = "组件名称")
    private String componentName;

    @Schema(description = "组件路径")
    private String componentPath;

    @Schema(description = "子节点数")
    private Integer subCount;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "租户id")
    private Long tenantId;


}
