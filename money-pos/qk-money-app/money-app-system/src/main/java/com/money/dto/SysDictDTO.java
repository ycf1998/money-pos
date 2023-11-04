package com.money.dto;

import com.money.common.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SysDictDTO {

    @Schema(description = "字典id")
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description="字典名")
    @NotBlank(message = "字典名不允许为空", groups = {ValidGroup.Save.class})
    private String name;

    @Schema(description="字典描述")
    @Size(max = 255, groups = {ValidGroup.Save.class})
    private String description;

}
