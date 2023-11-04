package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.dto.GmsGoodsCategory.GmsGoodsCategoryDTO;
import com.money.dto.SelectVO;
import com.money.dto.TreeNodeVO;
import com.money.entity.GmsGoodsCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
public interface GmsGoodsCategoryService extends IService<GmsGoodsCategory> {

    void add(GmsGoodsCategoryDTO addDTO, MultipartFile icon);

    void update(GmsGoodsCategoryDTO updateDTO, MultipartFile icon);

    void delete(Set<Long> ids);

    /**
     * 得到商品类别选择器
     *
     * @return {@link List}<{@link SelectVO}>
     */
    List<SelectVO> getGoodsCategorySelect();

    /**
     * 查询分类树
     *
     * @return {@link TreeNodeVO}
     */
    TreeNodeVO tree();

    /**
     * 得到所有子id
     *
     * @param pid pid
     * @return {@link List}<{@link Long}>
     */
    List<Long> getAllSubId(Long pid);

    /**
     * 更新商品数
     *
     * @param categoryId 类别id
     * @param step            步
     */
    void updateGoodsCount(Long categoryId, int step);
}
