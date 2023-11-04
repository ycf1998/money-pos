package com.money.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 资源权限表
 * </p>
 *
 * @author money
 * @since 2022-03-04
 */
@Data
public class SysPermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "名称")
    private String permissionName;

    @Schema(description = "资源类型")
    private String permissionType;

    @Schema(description = "父编码")
    private Long parentId;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "权限表达式")
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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子节点")
    private List<SysPermissionVO> children;

}
