package com.money.controller;

import com.money.common.dto.ValidGroup;
import com.money.dto.SysRoleDTO;
import com.money.dto.query.SysRoleQueryDTO;
import com.money.entity.SysRole;
import com.money.security.SecurityGuard;
import com.money.service.SysAuthService;
import com.money.service.SysRoleService;
import com.money.common.vo.PageVO;
import com.money.vo.SysRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Tag(name = "role", description = "角色管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/roles")
public class SysRoleController {

    private final SysAuthService sysAuthService;
    private final SysRoleService sysRoleService;

    @Operation(summary = "获取所有角色信息")
    @GetMapping("/all")
    @PreAuthorize("@rbac.hasPermission('role:list', 'user:add', 'user:edit')")
    public List<SysRole> getAllRoles() {
        return sysRoleService.getAll();
    }

    @Operation(summary = "分页查询角色信息")
    @GetMapping
    @PreAuthorize("@rbac.hasPermission('role:list')")
    public PageVO<SysRoleVO> listSysRole(@Validated SysRoleQueryDTO queryDTO) {
        return sysRoleService.list(queryDTO);
    }

    @Operation(summary = "添加角色信息")
    @PostMapping
    @PreAuthorize("@rbac.hasPermission('role:add')")
    public void addSysRole(@Validated(ValidGroup.Save.class) @RequestBody SysRoleDTO sysRoleDTO) {
        sysAuthService.checkLevel(SecurityGuard.getRbacUser().getUserId(), sysRoleDTO.getLevel());
        sysRoleService.add(sysRoleDTO);
    }

    @Operation(summary = "修改角色信息")
    @PutMapping
    @PreAuthorize("@rbac.hasPermission('role:edit')")
    public void updateSysRole(@Validated(ValidGroup.Update.class) @RequestBody SysRoleDTO sysRoleDTO) {
        sysAuthService.checkLevel(SecurityGuard.getRbacUser().getUserId(), sysRoleDTO.getLevel());
        sysAuthService.checkLevelForRole(SecurityGuard.getRbacUser().getUserId(), Collections.singleton(sysRoleDTO.getId()));
        sysRoleService.updateById(sysRoleDTO);
    }

    @Operation(summary = "删除角色信息")
    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('role:del')")
    public void deleteSysRole(@RequestBody Set<Long> ids) {
        sysAuthService.checkLevelForRole(SecurityGuard.getRbacUser().getUserId(), ids);
        sysRoleService.deleteById(ids);
    }

    @Operation(summary = "角色配置权限")
    @PostMapping("/{id}/permission")
    @PreAuthorize("@rbac.hasPermission('role:edit')")
    public void configurePermissions(@PathVariable Long id, @RequestBody Set<Long> permissions) {
        sysAuthService.checkLevelForRole(SecurityGuard.getRbacUser().getUserId(), Collections.singleton(id));
        sysRoleService.configurePermissions(id, permissions);
    }
}
