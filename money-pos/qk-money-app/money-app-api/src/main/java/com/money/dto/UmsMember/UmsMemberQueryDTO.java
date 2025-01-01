package com.money.dto.UmsMember;

import com.money.web.dto.PageQueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* <p>
* 会员表
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会员表")
public class UmsMemberQueryDTO extends PageQueryRequest {

    @Schema(description="卡号")
    private String code;

    @Schema(description="会员名称")
    private String name;

    @Schema(description="会员类型")
    private String type;

    @Schema(description="手机号")
    private String phone;
    
}
