package com.money.controller;


import com.money.common.dto.ValidGroup;
import com.money.dto.SysPermissionDTO;
import com.money.dto.query.SysPermissionQueryDTO;
import com.money.service.SysPermissionService;
import com.money.vo.SysPermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;


@Tag(name = "permission", description = "权限管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/permissions")
public class SysPermissionController {

    private final SysPermissionService sysPermissionService;

    @Operation(summary = "查询权限信息")
    @GetMapping
    @PreAuthorize("@rbac.hasPermission('permission:list', 'role:list')")
    public List<SysPermissionVO> listSysPermission(@Validated SysPermissionQueryDTO queryDTO) {
        return sysPermissionService.list(queryDTO);
    }

    @Operation(summary = "添加权限信息")
    @PostMapping
    @PreAuthorize("@rbac.hasPermission('permission:add')")
    public void addSysPermission(@Validated(ValidGroup.Save.class) @RequestBody SysPermissionDTO sysPermissionDTO) {
        sysPermissionService.add(sysPermissionDTO);
    }

    @Operation(summary = "修改权限信息")
    @PutMapping
    @PreAuthorize("@rbac.hasPermission('permission:edit')")
    public void updateSysPermission(@Validated(ValidGroup.Update.class) @RequestBody SysPermissionDTO sysPermissionDTO) {
        sysPermissionService.updateById(sysPermissionDTO);
    }

    @Operation(summary = "删除权限信息")
    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('permission:del')")
    public void delSysPermission(@RequestBody Set<Long> ids) {
        sysPermissionService.deleteById(ids);
    }

}
