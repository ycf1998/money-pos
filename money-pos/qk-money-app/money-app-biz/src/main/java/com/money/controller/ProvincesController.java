package com.money.controller;


import com.money.service.ProvincesService;
import com.money.dto.SelectVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class ProvincesController {

    private final ProvincesService provincesService;

    @GetMapping("/provinces")
    public List<SelectVO> listProvinces() {
        return provincesService.listProvinces();
    }

    @GetMapping("/cities")
    public List<SelectVO> listCities(@RequestParam String province) {
        return provincesService.listCities(province);
    }

    @GetMapping("/districts")
    public List<SelectVO> listDistricts(@RequestParam String city) {
        return provincesService.listDistricts(city);
    }
}
