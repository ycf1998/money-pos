package com.money.controller;


import com.money.common.dto.ValidGroup;
import com.money.common.vo.PageVO;
import com.money.service.GmsBrandService;
import com.money.dto.SelectVO;
import com.money.dto.brand.BrandDTO;
import com.money.dto.brand.BrandQueryDTO;
import com.money.dto.brand.BrandVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class GmsBrandController {

    public final GmsBrandService gmsBrandService;

    @GetMapping("/select")
    public List<SelectVO> brandSelect() {
        return gmsBrandService.getBrandSelect();
    }

    @GetMapping
    @PreAuthorize("@rbac.hasPermission('brand:list')")
    public PageVO<BrandVO> list(@Validated BrandQueryDTO queryDTO) {
        return gmsBrandService.list(queryDTO);
    }

    @PostMapping
    @PreAuthorize("@rbac.hasPermission('brand:add')")
    public Long addBrand(@Validated(ValidGroup.Save.class) @RequestPart("brand") BrandDTO brandDTO,
                         @RequestPart(required = false) MultipartFile logo) {
        return gmsBrandService.add(brandDTO, logo);
    }

    @PutMapping
    @PreAuthorize("@rbac.hasPermission('brand:edit')")
    public void updateBrand(@Validated(ValidGroup.Update.class) @RequestPart("brand") BrandDTO brandDTO,
                            @RequestPart(required = false) MultipartFile logo) {
        gmsBrandService.update(brandDTO, logo);
    }

    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('brand:del')")
    public void deleteBrand(@RequestBody Set<Long> ids) {
        gmsBrandService.delete(ids);
    }

}
