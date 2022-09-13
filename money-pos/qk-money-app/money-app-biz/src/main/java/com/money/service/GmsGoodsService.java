package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.common.vo.PageVO;
import com.money.entity.GmsGoods;
import com.money.dto.goods.GoodsDTO;
import com.money.dto.goods.GoodsQueryDTO;
import com.money.dto.goods.GoodsVO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author money
 * @since 2022-04-04
 */
public interface GmsGoodsService extends IService<GmsGoods> {

    /**
     * 查询商品列表
     *
     * @param queryDTO 查询dto
     * @return {@link PageVO}<{@link GoodsVO}>
     */
    PageVO<GoodsVO> list(GoodsQueryDTO queryDTO);

    /**
     * 添加商品
     *
     * @param goodsDTO 货物dto
     * @param pic     图片
     * @return {@link Long}
     */
    Long add(GoodsDTO goodsDTO, MultipartFile pic);

    /**
     * 更新商品
     *
     * @param goodsDTO 货物dto
     * @param pic     图片
     */
    void update(GoodsDTO goodsDTO, MultipartFile pic);

    /**
     * 删除商品
     *
     * @param ids id
     */
    void delete(Set<Long> ids);

    /**
     * 出售
     *
     * @param goodsId  商品id
     * @param quantity 数量
     */
    void sell(Long goodsId, Integer quantity);

    /**
     * 更新库存
     *
     * @param id       id
     * @param quantity 数量
     */
    void updateStock(Long id, Integer quantity);

    /**
     * 获得当前库存价值
     *
     * @return {@link BigDecimal}
     */
    BigDecimal getCurrentStockValue();
}
