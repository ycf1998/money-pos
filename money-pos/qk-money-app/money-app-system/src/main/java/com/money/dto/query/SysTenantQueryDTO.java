package com.money.dto.query;

import com.money.common.dto.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 系统租户查询dto
 * @createTime : 2022-03-26 10:31:29
 */
@Data
public class SysTenantQueryDTO extends QueryRequest {
    private static final long serialVersionUID = 1270698026052848385L;

    @Schema(description = "租户code")
    private String tenantCode;

    @Schema(description = "租户名称")
    private String tenantName;
}
