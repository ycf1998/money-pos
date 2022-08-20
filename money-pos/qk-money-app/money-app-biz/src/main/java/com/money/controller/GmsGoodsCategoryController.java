package com.money.controller;


import com.money.common.dto.ValidGroup;
import com.money.service.GmsGoodsCategoryService;
import com.money.vo.SelectVO;
import com.money.vo.TreeNodeVO;
import com.money.web.goodscategory.GoodsCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/goodsCategories")
@RequiredArgsConstructor
public class GmsGoodsCategoryController {

    public final GmsGoodsCategoryService gmsGoodsCategoriesService;

    @GetMapping("/select")
    public List<SelectVO> goodsCategorySelect() {
        return gmsGoodsCategoriesService.getGoodsCategorySelect();
    }

    @GetMapping("/tree")
    @PreAuthorize("@rbac.hasPermission('goods:list')")
    public TreeNodeVO tree() {
        return gmsGoodsCategoriesService.tree();
    }

    @PostMapping
    @PreAuthorize("@rbac.hasPermission('goods:add')")
    public Long addGoodsCategories(@Validated(ValidGroup.Save.class) @RequestPart("goodsCategory") GoodsCategoryDTO goodsCategoryDTO,
                         @RequestPart(required = false) MultipartFile icon) {
        return gmsGoodsCategoriesService.add(goodsCategoryDTO, icon);
    }

    @PutMapping
    @PreAuthorize("@rbac.hasPermission('goods:edit')")
    public void updateGoodsCategories(@Validated(ValidGroup.Update.class) @RequestPart("goodsCategory") GoodsCategoryDTO goodsCategoryDTO,
                            @RequestPart(required = false) MultipartFile icon) {
        gmsGoodsCategoriesService.update(goodsCategoryDTO, icon);
    }

    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('goods:del')")
    public void deleteGoodsCategories(@RequestBody Set<Long> ids) {
        gmsGoodsCategoriesService.delete(ids);
    }

}
