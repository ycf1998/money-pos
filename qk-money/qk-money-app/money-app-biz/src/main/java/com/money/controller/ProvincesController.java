package com.money.controller;

import com.money.dto.SelectVO;
import com.money.service.ProvincesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Tag(name = "provinces", description = "")
@RestController
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
