package com.money.testdata;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.constant.GoodsStatus;
import com.money.entity.GmsGoods;
import com.money.mapper.GmsGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 商品测试数据服务 - 负责商品数据的创建和清理
 */
@Component
@Scope("prototype")
public class GoodsDataService {

    @Autowired
    private GmsGoodsMapper goodsMapper;

    // 记录的测试商品 ID
    private final List<Long> createdGoodsIds = new ArrayList<>();

    /**
     * 创建测试商品（自动记录 ID，支持自定义）
     */
    public GmsGoods createGoods(String suffix, Consumer<GmsGoods> customizer) {
        GmsGoods goods = new GmsGoods();
        goods.setBrandId(1L);
        goods.setCategoryId(1L);
        goods.setBarcode("BARCODE_" + suffix);
        goods.setName("测试商品_" + suffix);
        goods.setPinyin("CESHISP_" + suffix);
        goods.setPic("https://example.com/goods_" + suffix + ".png");
        goods.setUnit("个");
        goods.setSize("标准规格");
        goods.setDescription("测试商品描述_" + suffix);
        goods.setPurchasePrice(new BigDecimal("10.00"));
        goods.setSalePrice(new BigDecimal("20.00"));
        goods.setVipPrice(new BigDecimal("18.00"));
        goods.setCoupon(new BigDecimal("2.00"));
        goods.setStock(100L);
        goods.setSales(0L);
        goods.setStatus(GoodsStatus.SALE.name());
        goods.setTenantId(100L);

        if (customizer != null) {
            customizer.accept(goods);
        }

        goodsMapper.insert(goods);
        createdGoodsIds.add(goods.getId());
        return goods;
    }

    /**
     * 创建测试商品（默认配置）
     */
    public GmsGoods createGoods(String suffix) {
        return createGoods(suffix, null);
    }

    /**
     * 清理创建的测试商品数据
     */
    public void cleanup() {
        try {
            // 1. 记录的商品 ID
            if (!createdGoodsIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(goodsMapper)
                        .in(GmsGoods::getId, createdGoodsIds)
                        .remove();
            }

            // 2. 兜底清理：所有测试商品_ 前缀的商品
            new LambdaUpdateChainWrapper<>(goodsMapper)
                    .likeRight(GmsGoods::getName, "测试商品_")
                    .remove();
        } catch (Exception e) {
            System.err.println("[GoodsDataService] 清理测试商品失败：" + e.getMessage());
        } finally {
            createdGoodsIds.clear();
        }
    }
}
