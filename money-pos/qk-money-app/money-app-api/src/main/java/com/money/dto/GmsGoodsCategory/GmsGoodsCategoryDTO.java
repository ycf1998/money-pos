package com.money.dto.GmsGoodsCategory;

import com.money.common.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class GmsGoodsCategoryDTO {

    @NotNull(groups = {ValidGroup.Update.class})
    private Long id;

    @Schema(description = "父id")
    @NotNull(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private Long pid;

    @Schema(description = "分类名称")
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String name;

}
