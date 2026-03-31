package com.money.dto;

import com.money.web.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 字典表 DTO
 *
 * @author money
 * @since 2024-05-18
 */
@Data
@Accessors(chain = true)
public class SysDictDTO {

    @Schema(description = "字典id")
    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description="字典名")
    @NotBlank(message = "字典名不允许为空", groups = {ValidGroup.Save.class})
    private String dictName;

    @Schema(description="字典描述")
    @Size(max = 255, groups = {ValidGroup.Save.class})
    private String dictDesc;

}
