package com.money.dto.GmsGoods;

import com.money.web.dto.PageQueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* <p>
* 商品表
* </p>
*
* @author money
* @since 2023-02-27
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品表")
public class GmsGoodsQueryDTO extends PageQueryRequest {

    @Schema(description="品牌id")
    private Long brandId;

    @Schema(description="分类id")
    private Long categoryId;

    @Schema(description="条码")
    private String barcode;

    @Schema(description="商品名称")
    private String name;

    @Schema(description="状态")
    private String status;
}
