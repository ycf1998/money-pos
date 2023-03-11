package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Getter
@Setter
@TableName("gms_goods_category")
@Schema(description = "商品分类表")
public class GmsGoodsCategory extends BaseEntity {

    @Schema(description="父分类id")
    private Long pid;

    @Schema(description="分类图标")
    private String icon;

    @Schema(description="分类名称")
    private String name;

    @Schema(description="商品数量")
    private Integer goodsCount;

    @Schema(description="租户id")
    private Long tenantId;

}
