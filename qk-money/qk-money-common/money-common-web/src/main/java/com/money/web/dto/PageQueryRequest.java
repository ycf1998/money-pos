package com.money.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.Map;

/**
 * 分页查询参数
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class PageQueryRequest extends PageRequest implements ISortRequest {

    @Schema(description = "排序 createTime,desc;id,asc")
    private String orderBy;

    @Override
    public Map<String, String> sortKeyMap() {
        return Collections.emptyMap();
    }
}
