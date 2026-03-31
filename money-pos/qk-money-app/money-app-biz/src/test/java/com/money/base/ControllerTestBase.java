package com.money.base;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.entity.SysUser;
import com.money.entity.SysUserRoleRelation;
import com.money.mapper.SysUserMapper;
import com.money.mapper.SysUserRoleRelationMapper;
import com.money.vo.AuthTokenVO;
import com.money.web.response.R;
import com.money.web.vo.PageVO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

/**
 * Controller 测试基类
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected SysUserMapper sysUserMapper;

    @Autowired
    protected SysUserRoleRelationMapper userRoleMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected String authToken;

    protected static final String TEST_USERNAME = "test_api";
    protected static final String TEST_PASSWORD = "123456";
    protected static final Long TEST_TENANT = 100L;

    @BeforeAll
    void createTestUser() {
        // 清理可能存在的旧数据：先查用户 ID
        SysUser existing = new LambdaQueryChainWrapper<>(sysUserMapper)
                .eq(SysUser::getUsername, TEST_USERNAME)
                .one();
        if (existing != null) {
            // 删除角色关联
            new LambdaUpdateChainWrapper<>(userRoleMapper)
                    .eq(SysUserRoleRelation::getUserId, existing.getId())
                    .remove();
            // 删除用户
            new LambdaUpdateChainWrapper<>(sysUserMapper)
                    .eq(SysUser::getId, existing.getId())
                    .remove();
        }

        // 创建新用户
        SysUser user = new SysUser();
        user.setUsername(TEST_USERNAME);
        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        user.setNickname("测试用户");
        user.setPhone("13800000000");
        user.setEnabled(true);
        user.setTenantId(TEST_TENANT);
        sysUserMapper.insert(user);

        // 关联角色
        SysUserRoleRelation relation = new SysUserRoleRelation();
        relation.setUserId(user.getId());
        relation.setRoleId(1L);
        userRoleMapper.insert(relation);

        // 登录
        loginAndGetToken();
    }

    @AfterAll
    void deleteTestUser() {
        // 先删除角色关联（通过 username 子查询）
        new LambdaUpdateChainWrapper<>(userRoleMapper)
                .inSql(SysUserRoleRelation::getUserId,
                        "SELECT id FROM sys_user WHERE username = '" + TEST_USERNAME + "'")
                .remove();

        // 再删除用户
        new LambdaUpdateChainWrapper<>(sysUserMapper)
                .eq(SysUser::getUsername, TEST_USERNAME)
                .remove();
    }

    private void loginAndGetToken() {
        try {
            Map<String, String> loginParams = Map.of("username", TEST_USERNAME, "password", TEST_PASSWORD);
            // 登录接口不需要 token，直接调用 request 且不传 Authorization 头
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginParams))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            assertSuccess(result);
            AuthTokenVO authTokenVO = getData(result, AuthTokenVO.class);
            this.authToken = authTokenVO.getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException("登录失败", e);
        }
    }

    // ==================== HTTP 调用方法 ====================

    /**
     * 统一请求方法
     */
    protected MvcResult request(HttpMethod method, String url, Object body, Map<String, String> params) throws Exception {
        var builder = MockMvcRequestBuilders.request(method, url)
                .header("Authorization", "Bearer " + authToken)
                .accept(MediaType.APPLICATION_JSON);

        if (body != null) {
            builder.contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body));
        }

        if (params != null) {
            params.forEach(builder::param);
        }

        return mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    protected MvcResult get(String url) throws Exception {
        return request(HttpMethod.GET, url, null, null);
    }

    protected MvcResult get(String url, Map<String, String> params) throws Exception {
        return request(HttpMethod.GET, url, null, params);
    }

    protected MvcResult post(String url, Object body) throws Exception {
        return request(HttpMethod.POST, url, body, null);
    }

    protected MvcResult put(String url, Object body) throws Exception {
        return request(HttpMethod.PUT, url, body, null);
    }

    protected MvcResult delete(String url, Object body) throws Exception {
        return request(HttpMethod.DELETE, url, body, null);
    }

    /**
     * Multipart 请求 - 用于 @RequestPart 接口
     *
     * @param method        HTTP 方法（POST/PUT）
     * @param url           请求路径
     * @param partName      JSON 部分的名称（如 "brand"）
     * @param dto           JSON 数据对象
     * @param filePartName  文件部分的名称（可为 null）
     * @param fileContent   文件内容（可为 null）
     */
    protected MvcResult multipart(HttpMethod method, String url, String partName, Object dto,
                                   String filePartName, byte[] fileContent) throws Exception {
        String json = objectMapper.writeValueAsString(dto);
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(method, url)
                .file(new MockMultipartFile(partName, partName + ".json", "application/json", json.getBytes("UTF-8")));
        builder.header("Authorization", "Bearer " + authToken);
        builder.accept(MediaType.APPLICATION_JSON);

        if (filePartName != null && fileContent != null) {
            builder.file(new MockMultipartFile(filePartName, filePartName, "application/octet-stream", fileContent));
        }

        return mockMvc.perform(builder).andReturn();
    }

    // ==================== 断言方法 ====================

    /**
     * 验证响应是否成功 (code == 200)
     */
    protected void assertSuccess(MvcResult result) throws Exception {
        R<?> r = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(R.class, Object.class)
        );
        Assertions.assertEquals(200, r.getCode(), "响应状态码应为 200");
    }

    /**
     * 获取响应数据 - 返回 R<T> 中的 data
     */
    protected <T> T getData(MvcResult result, Class<T> clazz) throws Exception {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(R.class, clazz);
        R<T> r = objectMapper.readValue(result.getResponse().getContentAsString(), type);
        return r.getData();
    }

    /**
     * 获取响应数据 - 返回 R<PageVO<T>> 中的 data
     */
    protected <T> PageVO<T> getPageData(MvcResult result, Class<T> clazz) throws Exception {
        JavaType pageType = objectMapper.getTypeFactory().constructParametricType(PageVO.class, clazz);
        JavaType type = objectMapper.getTypeFactory().constructParametricType(R.class, pageType);
        R<PageVO<T>> r = objectMapper.readValue(result.getResponse().getContentAsString(), type);
        return r.getData();
    }

    /**
     * 获取响应数据 - 返回 R<List<T>> 中的 data
     */
    protected <T> List<T> getListData(MvcResult result, Class<T> clazz) throws Exception {
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        JavaType type = objectMapper.getTypeFactory().constructParametricType(R.class, listType);
        R<List<T>> r = objectMapper.readValue(result.getResponse().getContentAsString(), type);
        return r.getData();
    }
}
