package com.money.service;

import com.money.dto.SelectVO;
import com.money.entity.GmsBrand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.money.web.vo.PageVO;
import com.money.dto.GmsBrand.GmsBrandDTO;
import com.money.dto.GmsBrand.GmsBrandQueryDTO;
import com.money.dto.GmsBrand.GmsBrandVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 商品品牌表 服务类
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
public interface GmsBrandService extends IService<GmsBrand> {

    PageVO<GmsBrandVO> list(GmsBrandQueryDTO queryDTO);

    void add(GmsBrandDTO addDTO, MultipartFile logo);

    void update(GmsBrandDTO updateDTO, MultipartFile logo);

    void delete(Set<Long> ids);

    /**
     * 获取品牌选择器
     *
     * @return {@link List}<{@link SelectVO}>
     */
    List<SelectVO> getBrandSelect();

    /**
     * 更新商品数
     *
     * @param id   品牌id
     * @param step 增/减商品数
     */
    void updateGoodsCount(Long id, int step);
}
