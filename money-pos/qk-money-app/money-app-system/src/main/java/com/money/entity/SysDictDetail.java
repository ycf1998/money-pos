package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典详情表
 * </p>
 *
 * @author money
 * @since 2024-05-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dict_detail")
public class SysDictDetail extends BaseEntity {

    @Schema(description = "所属字典")
    private String dict;

    @Schema(description = "字典值")
    private String value;

    @Schema(description = "中文描述")
    private String cnDesc;

    @Schema(description = "英文描述")
    private String enDesc;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "隐藏")
    private Boolean hidden;

}
