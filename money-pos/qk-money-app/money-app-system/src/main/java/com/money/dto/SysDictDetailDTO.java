package com.money.dto;

import com.money.common.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysDictDetailDTO {

    @Schema(description = "字典详情id")
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description = "字典名")
    @NotBlank(message = "字典名不允许为空", groups = {ValidGroup.Save.class})
    private String dict;

    @Schema(description = "字典标签")
    @NotBlank(message = "字典标签不允许为空", groups = {ValidGroup.Save.class})
    private String label;

    @Schema(description = "字典值")
    @NotBlank(message = "字典值不允许为空", groups = {ValidGroup.Save.class})
    private String value;

    @Schema(description = "排序")
    @NotNull(groups = {ValidGroup.Save.class})
    private Integer sort;
}
