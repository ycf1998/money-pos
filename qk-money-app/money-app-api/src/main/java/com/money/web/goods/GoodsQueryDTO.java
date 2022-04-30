package com.money.web.goods;

import com.money.constant.GoodsStatus;
import com.money.common.dto.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 商品查询dto
 * @createTime : 2022-04-04 12:52:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsQueryDTO extends QueryRequest {
    private static final long serialVersionUID = -1749782033296630283L;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 条码
     */
    private String barcode;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 状态
     */
    private GoodsStatus status;
}
