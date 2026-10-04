package com.money.testdata;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.GmsBrand;
import com.money.mapper.GmsBrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 品牌测试数据服务 - 负责品牌数据的创建和清理
 */
@Component
@Scope("prototype")
public class BrandDataService {

    @Autowired
    private GmsBrandMapper brandMapper;

    // 记录的测试品牌 ID
    private final List<Long> createdBrandIds = new ArrayList<>();

    /**
     * 创建测试品牌（自动记录 ID，支持自定义）
     */
    public GmsBrand createBrand(String suffix, Consumer<GmsBrand> customizer) {
        GmsBrand brand = new GmsBrand();
        brand.setName("测试品牌_" + suffix);
        brand.setDescription("测试品牌描述_" + suffix);
        brand.setLogo("https://example.com/logo_" + suffix + ".png");
        brand.setGoodsCount(0);
        brand.setTenantId(100L);

        if (customizer != null) {
            customizer.accept(brand);
        }

        brandMapper.insert(brand);
        createdBrandIds.add(brand.getId());
        return brand;
    }

    /**
     * 创建测试品牌（默认配置）
     */
    public GmsBrand createBrand(String suffix) {
        return createBrand(suffix, null);
    }

    /**
     * 清理创建的测试品牌数据
     */
    public void cleanup() {
        try {
            // 1. 删除记录的品牌 ID
            if (!createdBrandIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(brandMapper)
                        .in(GmsBrand::getId, createdBrandIds)
                        .remove();
            }

            // 2. 兜底清理：所有测试品牌_ 前缀的品牌
            new LambdaUpdateChainWrapper<>(brandMapper)
                    .likeRight(GmsBrand::getName, "测试品牌_")
                    .remove();
        } catch (Exception e) {
            System.err.println("[BrandDataService] 清理测试品牌失败：" + e.getMessage());
        } finally {
            createdBrandIds.clear();
        }
    }
}