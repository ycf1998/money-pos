package com.money.web.goods;

import com.money.constant.GoodsStatus;
import com.money.common.dto.ValidGroup;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 商品dto
 * @createTime : 2022-04-04 12:52:41
 */
@Data
public class GoodsDTO {

    @NotNull(groups = ValidGroup.Update.class)
    private Long id;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 标签
     */
    private String label;

    /**
     * 条码
     */
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String barcode;

    /**
     * 商品名称
     */
    @NotBlank(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private String name;

    /**
     * 商品拼音
     */
    private String pinyin;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String size;

    /**
     * 描述
     */
    private String description;

    /**
     * 进价
     */
    @NotNull(groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    @DecimalMin(value = "0", groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private BigDecimal purchasePrice;

    /**
     * 售价
     */
    @DecimalMin(value = "0", groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private BigDecimal salePrice;

    /**
     * 会员价
     */
    @DecimalMin(value = "0", groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private BigDecimal vipPrice;

    /**
     * 用券
     */
    @DecimalMin(value = "0", groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private BigDecimal coupon = BigDecimal.ZERO;

    /**
     * 库存
     */
    @Min(value = 0, groups = {ValidGroup.Save.class, ValidGroup.Update.class})
    private Long stock = 0L;

    /**
     * 状态
     */
    private GoodsStatus status = GoodsStatus.SALE;
}
