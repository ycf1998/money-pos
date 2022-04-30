package com.money.dto.query;

import com.money.common.dto.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 系统角色查询dto
 * @createTime : 2022-03-05 23:25:21
 */
@Data
public class SysRoleQueryDTO extends QueryRequest {

    private static final long serialVersionUID = -5513332778675097648L;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称/描述")
    private String name;

    @Schema(description = "角色级别")
    private Integer level;

    @Schema(description = "可用状态")
    private Boolean enabled;
}
