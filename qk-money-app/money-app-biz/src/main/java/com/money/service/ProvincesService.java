package com.money.service;

import com.money.entity.Provinces;
import com.baomidou.mybatisplus.extension.service.IService;
import com.money.vo.SelectVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author money
 * @since 2022-04-03
 */
public interface ProvincesService extends IService<Provinces> {

    /**
     * 省份列表
     *
     * @return {@link List}<{@link SelectVO}>
     */
    List<SelectVO> listProvinces();

    /**
     * 城市列表
     *
     * @param province 省
     * @return {@link List}<{@link SelectVO}>
     */
    List<SelectVO> listCities(String province);

    /**
     * 列表区
     *
     * @param city 城市
     * @return {@link List}<{@link SelectVO}>
     */
    List<SelectVO> listDistricts(String city);
}
