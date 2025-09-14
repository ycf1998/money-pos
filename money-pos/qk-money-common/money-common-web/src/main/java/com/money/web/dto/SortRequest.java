package com.money.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * 排序请求
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
public class SortRequest implements ISortRequest {

    @Schema(description = "排序 createTime,desc;id,asc")
    private String orderBy;

    @Override
    public Map<String, String> sortKeyMap() {
        return Collections.emptyMap();
    }
}
