package com.money.controller;


import com.money.common.dto.ValidGroup;
import com.money.common.vo.PageVO;
import com.money.service.GmsGoodsService;
import com.money.web.goods.GoodsDTO;
import com.money.web.goods.GoodsQueryDTO;
import com.money.web.goods.GoodsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GmsGoodsController {

    public final GmsGoodsService gmsGoodsService;

    @GetMapping
    @PreAuthorize("@rbac.hasPermission('goods:list')")
    public PageVO<GoodsVO> list(@Validated GoodsQueryDTO queryDTO) {
        return gmsGoodsService.list(queryDTO);
    }

    @PostMapping
    @PreAuthorize("@rbac.hasPermission('goods:add')")
    public Long addGoods(@Validated(ValidGroup.Save.class) @RequestPart("goods") GoodsDTO goodsDTO,
                         @RequestPart(required = false) MultipartFile pic) {
        return gmsGoodsService.add(goodsDTO, pic);
    }

    @PutMapping
    @PreAuthorize("@rbac.hasPermission('goods:edit')")
    public void updateGoods(@Validated(ValidGroup.Update.class) @RequestPart("goods") GoodsDTO goodsDTO,
                            @RequestPart(required = false) MultipartFile pic) {
        gmsGoodsService.update(goodsDTO, pic);
    }

    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('goods:del')")
    public void deleteGoods(@RequestBody Set<Long> ids) {
        gmsGoodsService.delete(ids);
    }
}
