package com.money.dto.query;

import com.money.web.dto.PageQueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统字典查询 DTO
 *
 * @author : money
 * @since : 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictPageQueryDTO extends PageQueryRequest {

    @Schema(description = "字典名称或者描述")
    private String nameOrDesc;

}
