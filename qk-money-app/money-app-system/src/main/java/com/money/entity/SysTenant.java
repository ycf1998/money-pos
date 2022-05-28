package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author money
 * @since 2022-03-04
 */
@Getter
@Setter
@TableName("sys_tenant")
public class SysTenant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "租户code")
    private String tenantCode;

    @Schema(description = "logo")
    private String logo;

    @Schema(description = "ico")
    private String ico;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户描述")
    private String tenantDesc;

    @Schema(description = "逻辑删除")
    private Boolean deleted;

    private Integer sort;


}
