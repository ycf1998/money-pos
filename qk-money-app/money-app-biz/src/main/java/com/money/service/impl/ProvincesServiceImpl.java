package com.money.service.impl;

import com.money.entity.Provinces;
import com.money.mapper.ProvincesMapper;
import com.money.service.ProvincesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.vo.SelectVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author money
 * @since 2022-04-03
 */
@Service
public class ProvincesServiceImpl extends ServiceImpl<ProvincesMapper, Provinces> implements ProvincesService {

    @Override
    public List<SelectVO> listProvinces() {
        return this.lambdaQuery().select(Provinces::getProvince).groupBy(Provinces::getProvince).list()
                .stream().map(provinces -> {
                    SelectVO vo = new SelectVO();
                    vo.setLabel(provinces.getProvince());
                    vo.setValue(provinces.getProvince());
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SelectVO> listCities(String province) {
        return this.lambdaQuery().select(Provinces::getCity).eq(Provinces::getProvince, province).groupBy(Provinces::getCity).list()
                .stream().map(provinces -> {
                    SelectVO vo = new SelectVO();
                    vo.setLabel(provinces.getCity());
                    vo.setValue(provinces.getCity());
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SelectVO> listDistricts(String city) {
        return this.lambdaQuery().select(Provinces::getDistrict).eq(Provinces::getCity, city).groupBy(Provinces::getDistrict).list()
                .stream().map(provinces -> {
                    SelectVO vo = new SelectVO();
                    vo.setLabel(provinces.getDistrict());
                    vo.setValue(provinces.getDistrict());
                    return vo;
                }).collect(Collectors.toList());
    }
}
