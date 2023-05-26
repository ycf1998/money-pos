package com.money.controller;


import com.money.common.dto.ValidGroup;
import com.money.dto.SysDictDTO;
import com.money.dto.SysDictDetailDTO;
import com.money.dto.query.SysDictQueryDTO;
import com.money.entity.SysDict;
import com.money.entity.SysDictDetail;
import com.money.service.SysDictDetailService;
import com.money.service.SysDictService;
import com.money.common.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Tag(name = "dict", description = "字典管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict")
public class SysDictController {

    private final SysDictService sysDictService;
    private final SysDictDetailService sysDictDetailService;

    @Operation(summary = "分页查询字典")
    @GetMapping
    @PreAuthorize("@rbac.hasPermission('dict:list')")
    public PageVO<SysDict> listSysDict(@Validated SysDictQueryDTO queryDTO) {
        return sysDictService.list(queryDTO);
    }

    @Operation(summary = "添加字典")
    @PostMapping
    @PreAuthorize("@rbac.hasPermission('dict:add')")
    public void addSysDict(@Validated(ValidGroup.Save.class) @RequestBody SysDictDTO sysDictDTO) {
        sysDictService.add(sysDictDTO);
    }

    @Operation(summary = "修改字典")
    @PutMapping
    @PreAuthorize("@rbac.hasPermission('dict:edit')")
    public void updateSysDict(@Validated(ValidGroup.Update.class) @RequestBody SysDictDTO sysDictDTO) {
        sysDictService.updateById(sysDictDTO);
    }

    @Operation(summary = "删除字典")
    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('dict:del')")
    public void deleteSysDict(@RequestBody Set<Long> ids) {
        sysDictService.deleteById(ids);
    }

    @Operation(summary = "获取字典详情")
    @GetMapping("/detail")
    public List<SysDictDetail> listSysDictDetail(@RequestParam String dict) {
        return sysDictDetailService.listByDict(dict);
    }

    @Operation(summary = "添加字典详情")
    @PostMapping("/detail")
    @PreAuthorize("@rbac.hasPermission('dict:edit')")
    public void addSysDictDetail(@Validated(ValidGroup.Save.class) @RequestBody SysDictDetailDTO sysDictDetailDTO) {
        sysDictDetailService.add(sysDictDetailDTO);
    }

    @Operation(summary = "修改字典详情")
    @PutMapping("/detail")
    @PreAuthorize("@rbac.hasPermission('dict:edit')")
    public void updateSysDictDetail(@Validated(ValidGroup.Update.class) @RequestBody SysDictDetailDTO sysDictDetailDTO) {
        sysDictDetailService.updateById(sysDictDetailDTO);
    }

    @Operation(summary = "删除字典详情")
    @DeleteMapping("/detail")
    @PreAuthorize("@rbac.hasPermission('dict:edit')")
    public void deleteSysDictDetail(@RequestBody Set<Long> ids) {
        sysDictDetailService.deleteById(ids);
    }
}
