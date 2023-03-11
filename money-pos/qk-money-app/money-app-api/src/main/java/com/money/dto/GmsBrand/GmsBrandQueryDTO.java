package com.money.dto.GmsBrand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.money.common.dto.QueryRequest;

/**
* <p>
* 商品品牌表
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品品牌表")
public class GmsBrandQueryDTO extends QueryRequest {

    @Schema(description="品牌名称")
    private String name;

}
