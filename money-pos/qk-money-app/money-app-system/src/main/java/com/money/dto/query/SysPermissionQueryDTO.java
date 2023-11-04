package com.money.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 系统角色查询dto
 * @createTime : 2022-03-05 23:25:21
 */
@Data
public class SysPermissionQueryDTO {

    @Schema(description = "parentId")
    private Long parentId;

    @Schema(description = "条件")
    private String condition;

    @Schema(description = "资源类型")
    private String permissionType;
}
