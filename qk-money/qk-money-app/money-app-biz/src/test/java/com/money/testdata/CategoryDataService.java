package com.money.testdata;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.GmsGoodsCategory;
import com.money.mapper.GmsGoodsCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类测试数据服务 - 负责分类数据的创建和清理
 */
@Component
@Scope("prototype")
public class CategoryDataService {

    @Autowired
    private GmsGoodsCategoryMapper categoryMapper;

    // 记录创建的测试分类 ID
    private final List<Long> createdCategoryIds = new ArrayList<>();

    /**
     * 创建测试分类（根分类，pid=0）
     */
    public GmsGoodsCategory createRootCategory(String suffix) {
        return createCategory(0L, suffix);
    }

    /**
     * 创建测试分类（子分类）
     */
    public GmsGoodsCategory createCategory(Long pid, String suffix) {
        GmsGoodsCategory category = new GmsGoodsCategory();
        category.setPid(pid);
        category.setName("测试分类_" + suffix);
        category.setGoodsCount(0);
        category.setTenantId(100L);

        categoryMapper.insert(category);
        createdCategoryIds.add(category.getId());
        return category;
    }

    /**
     * 清理创建的测试分类数据
     */
    public void cleanup() {
        try {
            // 1. 删除记录的分类 ID
            if (!createdCategoryIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(categoryMapper)
                        .in(GmsGoodsCategory::getId, createdCategoryIds)
                        .remove();
            }

            // 2. 兜底清理：所有测试分类_ 前缀的分类
            new LambdaUpdateChainWrapper<>(categoryMapper)
                    .likeRight(GmsGoodsCategory::getName, "测试分类_")
                    .remove();
        } catch (Exception e) {
            System.err.println("[CategoryDataService] 清理测试分类失败：" + e.getMessage());
        } finally {
            createdCategoryIds.clear();
        }
    }
}