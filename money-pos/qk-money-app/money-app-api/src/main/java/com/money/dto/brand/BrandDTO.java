package com.money.dto.brand;

import com.money.common.dto.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BrandDTO {

    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    /**
     * 品牌名称
     */
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String name;

    /**
     * 品牌描述
     */
    private String description;
}
