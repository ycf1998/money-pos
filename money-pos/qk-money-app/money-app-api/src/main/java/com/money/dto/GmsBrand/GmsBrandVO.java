package com.money.dto.GmsBrand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* <p>
* 商品品牌表
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@Schema(description = "商品品牌表")
public class GmsBrandVO {

    private Long id;

    @Schema(description="品牌logo")
    private String logo;

    @Schema(description="品牌名称")
    private String name;

    @Schema(description="品牌描述")
    private String description;

    @Schema(description="商品数量")
    private Integer goodsCount;

}
