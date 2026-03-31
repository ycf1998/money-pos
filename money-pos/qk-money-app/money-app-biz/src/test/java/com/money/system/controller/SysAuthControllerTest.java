package com.money.system.controller;

import com.money.base.ControllerTestBase;
import com.money.vo.AuthTokenVO;
import com.money.vo.UserInfoVO;
import com.money.vo.VueRouterVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 认证接口测试
 */
@DisplayName("认证接口测试")
class SysAuthControllerTest extends ControllerTestBase {

    @Nested
    @DisplayName("登录")
    class LoginTests {

        @Test
        @DisplayName("成功登录")
        void testLogin() throws Exception {
            var dto = Map.of(
                "username", TEST_USERNAME,
                "password", TEST_PASSWORD
            );
            MvcResult result = post("/auth/login", dto);
            assertSuccess(result);

            AuthTokenVO token = getData(result, AuthTokenVO.class);
            assertNotNull(token.getAccessToken());
            assertNotNull(token.getRefreshToken());
        }
    }

    @Nested
    @DisplayName("获取路由菜单")
    class RouterTests {

        @Test
        @DisplayName("成功获取 Vue 路由菜单")
        void testGetVueRouter() throws Exception {
            MvcResult result = get("/auth/router");
            assertSuccess(result);

            List<VueRouterVO> routers = getListData(result, VueRouterVO.class);
            assertNotNull(routers);
        }
    }

    @Nested
    @DisplayName("获取个人信息")
    class OwnInfoTests {

        @Test
        @DisplayName("成功获取个人信息")
        void testGetOwnInfo() throws Exception {
            MvcResult result = get("/auth/own");
            assertSuccess(result);

            UserInfoVO info = getData(result, UserInfoVO.class);
            assertNotNull(info);
        }
    }

    @Nested
    @DisplayName("刷新 Token")
    class RefreshTokenTests {

        @Test
        @DisplayName("成功刷新 Token")
        void testRefreshToken() throws Exception {
            // 重新登录获取新的 refresh token
            var loginDto = Map.of("username", TEST_USERNAME, "password", TEST_PASSWORD);
            MvcResult loginResult = post("/auth/login", loginDto);
            AuthTokenVO loginToken = getData(loginResult, AuthTokenVO.class);

            MvcResult result = get("/auth/refreshToken?refreshToken=" + loginToken.getRefreshToken());
            assertSuccess(result);

            AuthTokenVO newToken = getData(result, AuthTokenVO.class);
            assertNotNull(newToken.getAccessToken());
        }
    }

    @Nested
    @DisplayName("登出")
    class LogoutTests {

        @Test
        @DisplayName("成功登出")
        void testLogout() throws Exception {
            MvcResult result = post("/auth/logout", null);
            assertSuccess(result);
        }
    }
}
