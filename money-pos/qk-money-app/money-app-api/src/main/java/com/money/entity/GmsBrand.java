package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品品牌表
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Getter
@Setter
@TableName("gms_brand")
@Schema(description = "商品品牌表")
public class GmsBrand extends BaseEntity {

    @Schema(description="品牌logo")
    private String logo;

    @Schema(description="品牌名称")
    private String name;

    @Schema(description="品牌描述")
    private String description;

    @Schema(description="商品数量")
    private Integer goodsCount;

    @Schema(description="租户id")
    private Long tenantId;

}
