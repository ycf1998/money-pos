package com.money.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 分面VO
 * @createTime : 2022-03-10 23:20:50
 */
@Data
@AllArgsConstructor
@Schema(description = "分页VO")
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1894806388550035718L;

    @Schema(description = "当前页")
    private long current;

    @Schema(description = "页大小")
    private long size;

    @Schema(description = "总页数")
    private long pages;

    @Schema(description = "总条数")
    private long total;

    @Schema(description = "数据")
    private List<T> records;

}
