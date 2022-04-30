package com.money.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 分页请求
 * @createTime : 2022-02-12 11:44:31
 */
@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = -835853272876609729L;

    @Schema(required = true, description = "当前页")
    @Min(value = 1, message = "当前页最小为1")
    private long page;

    @Schema(required = true, description = "页大小")
    @Min(value = 1, message = "页大小最小为1")
    private long size;
}
