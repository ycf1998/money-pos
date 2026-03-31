package com.money.dto.query;

import com.money.web.dto.PageQueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统租户查询 DTO
 *
 * @author : money
 * @since : 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysTenantPageQueryDTO extends PageQueryRequest {

    @Schema(description = "租户 code")
    private String tenantCode;

    @Schema(description = "租户名称")
    private String tenantName;
}
