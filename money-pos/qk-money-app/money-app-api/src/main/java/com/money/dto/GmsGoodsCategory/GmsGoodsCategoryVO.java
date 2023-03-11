package com.money.dto.GmsGoodsCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* <p>
* 商品分类表
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@Schema(description = "商品分类表")
public class GmsGoodsCategoryVO {

    private Long id;

    @Schema(description="父分类id")
    private Long pid;

    @Schema(description="分类图标")
    private String icon;

    @Schema(description="分类名称")
    private String name;

    @Schema(description="商品数量")
    private Integer goodsCount;

}
