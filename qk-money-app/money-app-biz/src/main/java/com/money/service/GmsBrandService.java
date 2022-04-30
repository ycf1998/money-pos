package com.money.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.money.common.vo.PageVO;
import com.money.entity.GmsBrand;
import com.money.vo.SelectVO;
import com.money.web.brand.BrandDTO;
import com.money.web.brand.BrandQueryDTO;
import com.money.web.brand.BrandVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 商品品牌表 服务类
 * </p>
 *
 * @author money
 * @since 2022-04-04
 */
public interface GmsBrandService extends IService<GmsBrand> {

    /**
     * 获取品牌选择器
     *
     * @return {@link List}<{@link SelectVO}>
     */
    List<SelectVO> getBrandSelect();

    /**
     * 查询品牌列表
     *
     * @param queryDTO 查询dto
     * @return {@link PageVO}<{@link BrandVO}>
     */
    PageVO<BrandVO> list(BrandQueryDTO queryDTO);

    /**
     * 添加品牌
     *
     * @param brandDTO 品牌dto
     * @param logo     标志
     * @return {@link Long}
     */
    Long add(BrandDTO brandDTO, MultipartFile logo);

    /**
     * 更新品牌
     *
     * @param brandDTO 品牌dto
     * @param logo     标志
     */
    void update(BrandDTO brandDTO, MultipartFile logo);

    /**
     * 删除品牌
     *
     * @param ids id
     */
    void delete(Set<Long> ids);

    /**
     * 更新商品数
     *
     * @param brandId 品牌id
     * @param step    步
     */
    void updateGoodsCount(Long brandId, int step);

}
