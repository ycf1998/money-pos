package com.money.web.goodscategory;

import com.money.common.dto.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 商品类别dto
 * @createTime : 2022-04-04 21:31:16
 */
@Data
public class GoodsCategoryDTO {

    @NotNull(groups = {ValidGroup.Update.class})
    private Long id;

    /**
     * 父分类id
     */
    @NotNull(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private Long pid;

    /**
     * 分类名称
     */
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String name;

}
