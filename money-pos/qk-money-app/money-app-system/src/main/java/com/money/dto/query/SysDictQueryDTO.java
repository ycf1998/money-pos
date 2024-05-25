package com.money.dto.query;

import com.money.common.dto.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 系统字典查询dto
 * @createTime : 2022-03-25 22:37:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictQueryDTO extends QueryRequest {

    @Schema(description = "字典名称或者描述")
    private String nameOrDesc;

}
