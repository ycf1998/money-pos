package com.money.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询参数
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class PageQueryRequest extends PageRequest implements ISortRequest {

    /**
     * 排序 createTime,desc;id,asc;
     */
    @Schema(description = "排序 createTime,desc;id,asc;")
    private String orderBy;

}
