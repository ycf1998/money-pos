package com.money.controller;

import com.money.web.dto.ValidGroup;
import com.money.web.vo.PageVO;
import com.money.dto.GmsGoods.GmsGoodsDTO;
import com.money.dto.GmsGoods.GmsGoodsQueryDTO;
import com.money.dto.GmsGoods.GmsGoodsVO;
import com.money.service.GmsGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Tag(name = "gmsGoods", description = "商品表")
@RestController
@RequestMapping("/gms/goods")
@RequiredArgsConstructor
public class GmsGoodsController {

    private final GmsGoodsService gmsGoodsService;

    @Operation(summary = "分页查询")
    @GetMapping
    @PreAuthorize("@rbac.hasPermission('gmsGoods:list')")
    public PageVO<GmsGoodsVO> list(@Validated GmsGoodsQueryDTO queryDTO) {
        return gmsGoodsService.list(queryDTO);
    }

    @Operation(summary = "添加")
    @PostMapping
    @PreAuthorize("@rbac.hasPermission('gmsGoods:add')")
    public void add(@Validated(ValidGroup.Save.class) @RequestPart("goods") GmsGoodsDTO addDTO,
                    @RequestPart(required = false) MultipartFile pic) {
        gmsGoodsService.add(addDTO, pic);
    }

    @Operation(summary = "修改")
    @PutMapping
    @PreAuthorize("@rbac.hasPermission('gmsGoods:edit')")
    public void update(@Validated(ValidGroup.Update.class) @RequestPart("goods") GmsGoodsDTO updateDTO,
                       @RequestPart(required = false) MultipartFile pic) {
        gmsGoodsService.update(updateDTO, pic);
    }

    @Operation(summary = "删除")
    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('gmsGoods:del')")
    public void delete(@RequestBody Set<Long> ids) {
        gmsGoodsService.delete(ids);
    }
}
