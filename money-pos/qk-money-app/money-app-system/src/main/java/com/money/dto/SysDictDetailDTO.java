package com.money.dto;

import com.money.web.dto.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 字典详情表
* </p>
*
* @author money
* @since 2024-05-18
*/
@Data
@Accessors(chain = true)
public class SysDictDetailDTO {

    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    @Schema(description = "所属字典")
    @NotBlank(message = "所属字典不允许为空", groups = {ValidGroup.Save.class})
    private String dict;

    @Schema(description = "字典值")
    @NotBlank(message = "字典值不允许为空", groups = {ValidGroup.Save.class})
    private String value;

    @Schema(description = "中文描述")
    @NotBlank(message = "中文描述不允许为空", groups = {ValidGroup.Save.class})
    private String cnDesc;

    @Schema(description = "英文描述")
    private String enDesc;

    @Schema(description = "是否隐藏")
    private Boolean hidden;

}
