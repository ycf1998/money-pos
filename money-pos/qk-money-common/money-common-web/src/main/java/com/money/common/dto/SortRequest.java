package com.money.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 排序请求
 * @createTime : 2022-10-01 11:52:33
 */
@Data
public class SortRequest implements ISortRequest {

    private static final long serialVersionUID = 4432103394522401613L;

    @Schema(description = "排序 createTime,desc;id,asc;")
    private String sort;
}
