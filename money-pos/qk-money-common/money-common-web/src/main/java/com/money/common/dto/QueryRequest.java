package com.money.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 查询请求
 * @createTime : 2022-03-05 12:19:01
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class QueryRequest extends PageRequest implements ISortRequest {

    @Schema(description = "排序 createTime,desc;id,asc;")
    private String sort;

}
