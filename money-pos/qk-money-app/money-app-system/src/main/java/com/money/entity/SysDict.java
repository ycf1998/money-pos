package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author money
 * @since 2024-05-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dict")
public class SysDict extends BaseEntity {

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典描述")
    private String dictDesc;
}
