package com.money.controller;


import com.money.common.dto.ValidGroup;
import com.money.dto.ChangePasswordDTO;
import com.money.dto.SysUserDTO;
import com.money.dto.UpdateProfileDTO;
import com.money.dto.query.SysUserQueryDTO;
import com.money.security.SecurityGuard;
import com.money.security.annotation.CurrentUser;
import com.money.service.SysRoleService;
import com.money.service.SysUserService;
import com.money.common.vo.PageVO;
import com.money.vo.SysUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Set;

@Tag(name = "sysUser", description = "用户管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;

    @Operation(summary = "上传头像", tags = {"sysUser"})
    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@CurrentUser String username, MultipartFile file) {
        return sysUserService.uploadAvatar(username, file);
    }

    @Operation(summary = "更新资料", tags = {"sysUser"})
    @PostMapping("/updateProfile")
    public void updateProfile(@CurrentUser String username, @Validated @RequestBody UpdateProfileDTO updateProfileDTO) {
        sysUserService.updateProfile(username, updateProfileDTO);
    }

    @Operation(summary = "修改密码", tags = {"sysUser"})
    @PostMapping("/changePassword")
    public void changePassword(@CurrentUser String username, @Validated @RequestBody ChangePasswordDTO changePasswordDTO) {
        sysUserService.changePassword(username, changePasswordDTO);
    }

    @Operation(summary = "分页查询用户信息", tags = {"sysUser"})
    @GetMapping
    @PreAuthorize("@rbac.hasPermission('user:list')")
    public PageVO<SysUserVO> listSysUser(@Validated SysUserQueryDTO queryDTO) {
        return sysUserService.list(queryDTO);
    }

    @Operation(summary = "添加用户", tags = {"sysUser"})
    @PostMapping
    @PreAuthorize("@rbac.hasPermission('user:add')")
    public void addSysUser(@Validated(ValidGroup.Save.class) @RequestBody SysUserDTO sysUserDTO) {
        sysRoleService.checkLevelByRoleId(SecurityGuard.getRbacUser().getUserId(), sysUserDTO.getRoles());
        sysUserService.add(sysUserDTO);
    }

    @Operation(summary = "修改用户", tags = {"sysUser"})
    @PutMapping
    @PreAuthorize("@rbac.hasPermission('user:edit')")
    public void updateSysUser(@Validated(ValidGroup.Update.class) @RequestBody SysUserDTO sysUserDTO) {
        sysRoleService.checkLevelByUserId(SecurityGuard.getRbacUser().getUserId(), Collections.singleton(sysUserDTO.getId()));
        sysRoleService.checkLevelByRoleId(SecurityGuard.getRbacUser().getUserId(), sysUserDTO.getRoles());
        sysUserService.updateById(sysUserDTO);
    }

    @Operation(summary = "删除用户", tags = {"sysUser"})
    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('user:del')")
    public void deleteSysUser(@RequestBody Set<Long> ids) {
        sysRoleService.checkLevelByUserId(SecurityGuard.getRbacUser().getUserId(), ids);
        sysUserService.deleteById(ids);
    }

    @Operation(summary = "更改用户状态", tags = {"sysUser"})
    @PostMapping("/changeStatus")
    @PreAuthorize("@rbac.hasPermission('user:edit')")
    public void changeStatus(@RequestParam Long id, @RequestParam Boolean enabled) {
        sysRoleService.checkLevelByUserId(SecurityGuard.getRbacUser().getUserId(), Collections.singleton(id));
        sysUserService.changeStatus(id, enabled);
    }
}
