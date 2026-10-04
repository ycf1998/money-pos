package com.money.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.money.mb.base.BaseEntity;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Getter
@Setter
@TableName("gms_goods")
@Schema(description = "商品表")
public class GmsGoods extends BaseEntity {

    @Schema(description="品牌id")
    private Long brandId;

    @Schema(description="分类id")
    private Long categoryId;

    @Schema(description="条码")
    private String barcode;

    @Schema(description="商品名称")
    private String name;

    @Schema(description="商品拼音")
    private String pinyin;

    @Schema(description="商品图片")
    private String pic;

    @Schema(description="单位")
    private String unit;

    @Schema(description="规格")
    private String size;

    @Schema(description="描述")
    private String description;

    @Schema(description="进价")
    private BigDecimal purchasePrice;

    @Schema(description="售价")
    private BigDecimal salePrice;

    @Schema(description="会员价")
    private BigDecimal vipPrice;

    @Schema(description="用券")
    private BigDecimal coupon;

    @Schema(description="库存")
    private Long stock;

    @Schema(description="销量")
    private Long sales;

    @Schema(description="状态")
    private String status;

    @Schema(description="租户id")
    private Long tenantId;

}
