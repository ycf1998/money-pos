package com.money.service;

import com.money.entity.GmsGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.money.web.vo.PageVO;
import com.money.dto.GmsGoods.GmsGoodsDTO;
import com.money.dto.GmsGoods.GmsGoodsQueryDTO;
import com.money.dto.GmsGoods.GmsGoodsVO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Set;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
public interface GmsGoodsService extends IService<GmsGoods> {

    PageVO<GmsGoodsVO> list(GmsGoodsQueryDTO queryDTO);

    void add(GmsGoodsDTO addDTO, MultipartFile pic);

    void update(GmsGoodsDTO updateDTO, MultipartFile pic);

    void delete(Set<Long> ids);

    /**
     * 出售
     *
     * @param goodsId 商品id
     * @param qty     数量
     */
    void sell(Long goodsId, Integer qty);

    /**
     * 更新库存
     *
     * @param goodsId 商品id
     * @param qty     数量
     */
    void updateStock(Long goodsId, Integer qty);

    /**
     * 获得当前股票价值
     *
     * @return {@link BigDecimal}
     */
    BigDecimal getCurrentStockValue();
}
