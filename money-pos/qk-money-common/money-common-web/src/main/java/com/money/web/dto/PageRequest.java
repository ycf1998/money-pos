package com.money.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 分页请求
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
public class PageRequest {

    @Schema(description = "当前页")
    @Min(value = 1, message = "当前页最小为1")
    private long page = 1;

    @Schema(description = "页大小")
    @Min(value = 1, message = "页大小最小为1")
    private long size = 10;
}
