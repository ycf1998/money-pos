package com.money.controller;

import com.money.common.dto.ValidGroup;
import com.money.dto.GmsGoodsCategory.GmsGoodsCategoryDTO;
import com.money.dto.SelectVO;
import com.money.dto.TreeNodeVO;
import com.money.service.GmsGoodsCategoryService;
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
 * 商品分类表 前端控制器
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Tag(name = "gmsGoodsCategory", description = "商品分类表")
@RestController
@RequestMapping("/gms/goodsCategory")
@RequiredArgsConstructor
public class GmsGoodsCategoryController {

    private final GmsGoodsCategoryService gmsGoodsCategoryService;

    @GetMapping("/select")
    public List<SelectVO> goodsCategorySelect() {
        return gmsGoodsCategoryService.getGoodsCategorySelect();
    }

    @GetMapping("/tree")
    @PreAuthorize("@rbac.hasPermission('gmsGoods:list')")
    public TreeNodeVO tree() {
        return gmsGoodsCategoryService.tree();
    }

    @PostMapping
    @PreAuthorize("@rbac.hasPermission('gmsGoods:add')")
    public void addGoodsCategories(@Validated(ValidGroup.Save.class) @RequestPart("goodsCategory") GmsGoodsCategoryDTO goodsCategoryDTO,
                                   @RequestPart(required = false) MultipartFile icon) {
        gmsGoodsCategoryService.add(goodsCategoryDTO, icon);
    }

    @PutMapping
    @PreAuthorize("@rbac.hasPermission('gmsGoods:edit')")
    public void updateGoodsCategories(@Validated(ValidGroup.Update.class) @RequestPart("goodsCategory") GmsGoodsCategoryDTO goodsCategoryDTO,
                                      @RequestPart(required = false) MultipartFile icon) {
        gmsGoodsCategoryService.update(goodsCategoryDTO, icon);
    }

    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('gmsGoods:del')")
    public void deleteGoodsCategories(@RequestBody Set<Long> ids) {
        gmsGoodsCategoryService.delete(ids);
    }
}
