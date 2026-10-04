package com.money.dto.query;

import com.money.web.dto.PageQueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统角色查询 DTO
 *
 * @author : money
 * @since : 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRolePageQueryDTO extends PageQueryRequest {

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称/描述")
    private String name;

    @Schema(description = "角色级别")
    private Integer level;

    @Schema(description = "可用状态")
    private Boolean enabled;
}
