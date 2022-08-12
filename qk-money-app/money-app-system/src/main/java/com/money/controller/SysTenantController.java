package com.money.controller;

import com.money.common.dto.ValidGroup;
import com.money.dto.SysTenantDTO;
import com.money.dto.query.SysTenantQueryDTO;
import com.money.entity.SysTenant;
import com.money.service.SysTenantService;
import com.money.common.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Tag(name = "tenant", description = "租户管理")
@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class SysTenantController {

    private final SysTenantService sysTenantService;

    @Operation(summary = "通过code获取租户")
    @GetMapping("/byCode")
    public SysTenant getTenant(@RequestParam String code) {
        return sysTenantService.getTenantIdByCode(code);
    }

    @Operation(summary = "分页查询租户")
    @GetMapping
    @PreAuthorize("@rbac.hasPermission('tenant:list')")
    public PageVO<SysTenant> listSysTenant(SysTenantQueryDTO queryDTO) {
        return sysTenantService.list(queryDTO);
    }

    @Operation(summary = "添加租户")
    @PostMapping
    @PreAuthorize("@rbac.hasPermission('tenant:add')")
    public void addSysTenant(@Validated(ValidGroup.Save.class) @RequestPart("tenant") SysTenantDTO sysTenantDTO,
                             @RequestPart(required = false) MultipartFile logo) {
        sysTenantService.add(sysTenantDTO, logo);
    }

    @Operation(summary = "修改租户")
    @PutMapping
    @PreAuthorize("@rbac.hasPermission('tenant:edit')")
    public void updateSysTenant(@Validated(ValidGroup.Update.class) @RequestPart("tenant") SysTenantDTO sysTenantDTO,
                                @RequestPart(required = false) MultipartFile logo) {
        sysTenantService.updateById(sysTenantDTO, logo);
    }

    @Operation(summary = "删除租户")
    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('tenant:del')")
    public void deleteSysTenant(@RequestBody Set<Long> ids) {
        sysTenantService.deleteById(ids);
    }
}
