package com.money.controller;

import com.money.dto.LoginDTO;
import com.money.security.annotation.CurrentUser;
import com.money.service.SysUserService;
import com.money.vo.AuthTokenVO;
import com.money.vo.OwnInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "auth", description = "认证访问")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SysAuthController {

    private final SysUserService sysUserService;

    @Operation(summary = "登录", tags = {"auth"})
    @PostMapping("/login")
    public AuthTokenVO login(@Validated @RequestBody LoginDTO loginDto) {
        return sysUserService.login(loginDto);
    }

    @Operation(summary = "注销", tags = {"auth"})
    @PostMapping("/logout")
    public void logout(@Parameter(hidden = true) @RequestHeader("${money.security.token.header}") String token) {
        sysUserService.logout(token);
    }

    @Operation(summary = "获取自己的信息", tags = {"auth"})
    @GetMapping("/own")
    public OwnInfoVO own(@Parameter(hidden = true) @CurrentUser String username) {
        return sysUserService.getOwnInfo(username);
    }

    @Operation(summary = "刷新令牌", tags = {"auth"})
    @GetMapping("/refreshToken")
    public AuthTokenVO refreshToken(String refreshToken) {
        return sysUserService.refreshToken(refreshToken);
    }

}
