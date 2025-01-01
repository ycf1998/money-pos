package com.money.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页 VO
 *
 * @author : money
 * @since : 1.0.0
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

    @Schema(description = "总条数")
    private long total;

    @Schema(description = "数据")
    private List<T> records;

    @Schema(description = "总页数")
    public long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }
}
