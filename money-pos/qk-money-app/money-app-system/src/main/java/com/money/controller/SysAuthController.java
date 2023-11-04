package com.money.controller;

import com.money.dto.LoginDTO;
import com.money.security.annotation.CurrentUser;
import com.money.service.SysAuthService;
import com.money.vo.AuthTokenVO;
import com.money.vo.UserInfoVO;
import com.money.vo.VueRouterVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "auth", description = "认证访问")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SysAuthController {

    private final SysAuthService sysAuthService;

    @Operation(summary = "获取VUE菜单")
    @GetMapping("/router")
    public List<VueRouterVO> getVueRouter(@CurrentUser Long userId) {
        return sysAuthService.getVueRouter(userId);
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public AuthTokenVO login(@Validated @RequestBody LoginDTO loginDto) {
        return sysAuthService.login(loginDto);
    }

    @Operation(summary = "注销")
    @PostMapping("/logout")
    public void logout(@Parameter(hidden = true) @RequestHeader("${money.security.token.header}") String token) {
        sysAuthService.logout(token);
    }

    @Operation(summary = "获取个人信息")
    @GetMapping("/own")
    public UserInfoVO own(@Parameter(hidden = true) @CurrentUser String username) {
        return sysAuthService.getUserInfo(username);
    }

    @Operation(summary = "刷新令牌")
    @GetMapping("/refreshToken")
    public AuthTokenVO refreshToken(@Parameter(hidden = true) @CurrentUser String username, String refreshToken) {
        return sysAuthService.refreshToken(username, refreshToken);
    }

}
