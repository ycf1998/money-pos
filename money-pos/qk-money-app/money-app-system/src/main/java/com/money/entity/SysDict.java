package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author money
 * @since 2022-03-05
 */
@Getter
@Setter
@TableName("sys_dict")
@Schema(description = "SysDict对象")
public class SysDict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "字典名")
    private String name;

    @Schema(description = "字典描述")
    private String description;

    @Schema(description = "排序")
    private Integer sort;

}
