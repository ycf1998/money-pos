package com.money.controller;

import com.money.common.dto.ValidGroup;
import com.money.common.vo.PageVO;
import com.money.dto.GmsBrand.GmsBrandDTO;
import com.money.dto.GmsBrand.GmsBrandQueryDTO;
import com.money.dto.GmsBrand.GmsBrandVO;
import com.money.dto.SelectVO;
import com.money.service.GmsBrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 商品品牌表 前端控制器
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Tag(name = "gmsBrand", description = "商品品牌表")
@RestController
@RequestMapping("/gms/brand")
@RequiredArgsConstructor
public class GmsBrandController {

    private final GmsBrandService gmsBrandService;

    @GetMapping("/select")
    public List<SelectVO> brandSelect() {
        return gmsBrandService.getBrandSelect();
    }

    @GetMapping
    @PreAuthorize("@rbac.hasPermission('gmsBrand:list')")
    public PageVO<GmsBrandVO> list(@Validated GmsBrandQueryDTO queryDTO) {
        return gmsBrandService.list(queryDTO);
    }

    @PostMapping
    @PreAuthorize("@rbac.hasPermission('gmsBrand:add')")
    public void add(@Validated(ValidGroup.Save.class) @RequestPart("brand") GmsBrandDTO brandDTO,
                         @RequestPart(required = false) MultipartFile logo) {
        gmsBrandService.add(brandDTO, logo);
    }

    @PutMapping
    @PreAuthorize("@rbac.hasPermission('gmsBrand:edit')")
    public void update(@Validated(ValidGroup.Update.class) @RequestPart("brand") GmsBrandDTO brandDTO,
                            @RequestPart(required = false) MultipartFile logo) {
        gmsBrandService.update(brandDTO, logo);
    }

    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('gmsBrand:del')")
    public void delete(@RequestBody Set<Long> ids) {
        gmsBrandService.delete(ids);
    }
}
