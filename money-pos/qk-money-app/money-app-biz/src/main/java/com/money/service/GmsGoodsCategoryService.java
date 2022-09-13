package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.dto.goodscategory.GoodsCategoryDTO;
import com.money.entity.GmsGoodsCategory;
import com.money.dto.SelectVO;
import com.money.dto.TreeNodeVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author money
 * @since 2022-04-04
 */
public interface GmsGoodsCategoryService extends IService<GmsGoodsCategory> {

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
     * 添加分类
     *
     * @param goodsCategoryDTO 商品类别dto
     * @param icon             图标
     * @return {@link Long}
     */
    Long add(GoodsCategoryDTO goodsCategoryDTO, MultipartFile icon);

    /**
     * 更新分类
     *
     * @param goodsCategoryDTO 商品类别dto
     * @param icon             图标
     */
    void update(GoodsCategoryDTO goodsCategoryDTO, MultipartFile icon);

    /**
     * 删除分类
     *
     * @param ids id
     */
    void delete(Set<Long> ids);

    /**
     * 更新商品数
     *
     * @param categoryId 类别id
     * @param step            步
     */
    void updateGoodsCount(Long categoryId, int step);

}
