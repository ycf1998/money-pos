package com.money.dto.GmsBrand;

import com.money.common.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class GmsBrandDTO {

    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description="品牌名称")
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String name;

    @Schema(description="品牌描述")
    private String description;

}
