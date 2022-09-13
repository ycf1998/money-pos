package com.money.dto.brand;

import com.money.common.dto.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BrandQueryDTO extends QueryRequest {
    private static final long serialVersionUID = 374658360865197229L;

    /**
     * 品牌名称
     */
    private String name;

}
