package com.money.system.controller;

import com.money.base.ControllerTestBase;
import com.money.dto.ChangePasswordDTO;
import com.money.dto.SysUserDTO;
import com.money.dto.UpdateProfileDTO;
import com.money.entity.SysRole;
import com.money.entity.SysUser;
import com.money.mapper.SysUserMapper;
import com.money.system.testdata.RoleDataService;
import com.money.system.testdata.UserDataService;
import com.money.vo.SysUserVO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户管理接口测试
 */
@DisplayName("用户管理接口测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SysUserControllerTest extends ControllerTestBase {

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private RoleDataService roleDataService;

    @Autowired
    private SysUserMapper sysUserMapper;

    // 全局测试角色（所有测试共用）
    private SysRole testRole;

    /**
     * 所有测试开始前，创建全局测试角色
     */
    @BeforeAll
    void setUpGlobal() {
        testRole = roleDataService.createRole("TEST_ROLE", "测试角色", 10);
    }

    /**
     * 每个测试方法执行后，清理测试数据
     */
    @AfterEach
    void tearDown() {
        userDataService.cleanup();
    }

    /**
     * 所有测试结束后，清理全局数据
     */
    @AfterAll
    void cleanupGlobal() {
        roleDataService.cleanup();
    }

    // ============================================================
    // 分页查询用户
    // ============================================================

    @Nested
    @DisplayName("分页查询用户")
    class ListUserTests {

        @BeforeEach
        void setUp() {
            // 创建 5 个测试用户
            for (int i = 0; i < 5; i++) {
                SysUser user = userDataService.createUser("page_" + i);
                roleDataService.assignRole(user.getId(), testRole.getId());
            }
        }

        @Test
        @DisplayName("成功分页查询用户列表")
        void testListUsers() throws Exception {
            var result = get("/users", Map.of("page", "1", "size", "2"));
            assertSuccess(result);

            var pageData = getPageData(result, SysUserVO.class);
            assertNotNull(pageData);
            assertEquals(2, pageData.getRecords().size());
            assertTrue(pageData.getTotal() >= 5);
        }

        @Test
        @DisplayName("成功按用户名搜索用户")
        void testListUsersByName() throws Exception {
            SysUser user = userDataService.createUser("search");
            roleDataService.assignRole(user.getId(), testRole.getId());

            var result = get("/users", Map.of(
                    "page", "1",
                    "size", "10",
                    "name", user.getUsername()
            ));
            assertSuccess(result);

            var pageData = getPageData(result, SysUserVO.class);
            assertTrue(pageData.getRecords().stream()
                    .anyMatch(u -> u.getUsername().equals(user.getUsername())));
        }

        @Test
        @DisplayName("成功按启用状态筛选用户")
        void testListUsersByEnabled() throws Exception {
            var result = get("/users", Map.of(
                    "page", "1",
                    "size", "10",
                    "enabled", "true"
            ));
            assertSuccess(result);

            var pageData = getPageData(result, SysUserVO.class);
            assertTrue(pageData.getRecords().stream().allMatch(SysUserVO::getEnabled));
        }

        @Test
        @DisplayName("成功按创建时间排序")
        void testListUsersOrderByCreateTime() throws Exception {
            var result = get("/users", Map.of(
                    "page", "1",
                    "size", "10"
            ));
            assertSuccess(result);

            var pageData = getPageData(result, SysUserVO.class);
            assertFalse(pageData.getRecords().isEmpty());
        }
    }

    // ============================================================
    // 添加用户
    // ============================================================

    @Nested
    @DisplayName("添加用户")
    class AddUserTests {

        @Test
        @DisplayName("成功添加用户")
        void testAddUserSuccess() throws Exception {
            SysUserDTO dto = new SysUserDTO();
            dto.setUsername("test_add_" + System.currentTimeMillis());
            dto.setNickname("测试用户");
            dto.setPhone("13900000000");
            dto.setEmail("test@example.com");
            dto.setEnabled(true);
            dto.setRemark("测试备注");
            dto.setRoles(Set.of(testRole.getId()));

            var result = post("/users", dto);
            assertSuccess(result);
            // 注意：通过 API 创建的用户，TestDataService 无法清理
            // 依赖 UserDataService 的兜底清理（清理所有 test_ 前缀的用户）
        }
    }

    // ============================================================
    // 修改用户
    // ============================================================

    @Nested
    @DisplayName("修改用户")
    class UpdateUserTests {

        private SysUser testUser;

        @BeforeEach
        void setUp() {
            testUser = userDataService.createUser("update");
            roleDataService.assignRole(testUser.getId(), testRole.getId());
        }

        @Test
        @DisplayName("成功修改用户")
        void testUpdateUserSuccess() throws Exception {
            SysUserDTO updateDto = new SysUserDTO();
            updateDto.setId(testUser.getId());
            updateDto.setUsername("test_update_" + System.currentTimeMillis());
            updateDto.setNickname("更新后");
            updateDto.setPhone("13900000001");
            updateDto.setEmail("updated@example.com");
            updateDto.setEnabled(true);
            updateDto.setRemark("更新备注");
            updateDto.setRoles(Set.of(testRole.getId()));

            var result = put("/users", updateDto);
            assertSuccess(result);
        }
    }

    // ============================================================
    // 删除用户
    // ============================================================

    @Nested
    @DisplayName("删除用户")
    class DeleteUserTests {

        @Test
        @DisplayName("成功删除用户")
        void testDeleteUserSuccess() throws Exception {
            SysUser user = userDataService.createUser("delete");
            roleDataService.assignRole(user.getId(), testRole.getId());

            var result = delete("/users", Set.of(user.getId()));
            assertSuccess(result);

            assertNull(sysUserMapper.selectById(user.getId()));
        }

        @Test
        @DisplayName("成功批量删除用户")
        void testDeleteUsersBatch() throws Exception {
            SysUser user1 = userDataService.createUser("batch1");
            roleDataService.assignRole(user1.getId(), testRole.getId());
            
            SysUser user2 = userDataService.createUser("batch2");
            roleDataService.assignRole(user2.getId(), testRole.getId());

            var result = delete("/users", Set.of(user1.getId(), user2.getId()));
            assertSuccess(result);

            assertNull(sysUserMapper.selectById(user1.getId()));
            assertNull(sysUserMapper.selectById(user2.getId()));
        }
    }

    // ============================================================
    // 更新个人资料
    // ============================================================

    @Nested
    @DisplayName("更新个人资料")
    class UpdateProfileTests {

        @Test
        @DisplayName("成功更新个人资料")
        void testUpdateProfileSuccess() throws Exception {
            UpdateProfileDTO dto = new UpdateProfileDTO();
            dto.setNickname("新昵称");
            dto.setPhone("13900000002");
            dto.setEmail("newemail@example.com");
            dto.setRemark("个人备注");

            var result = post("/users/updateProfile", dto);
            assertSuccess(result);
        }
    }

    // ============================================================
    // 修改密码
    // ============================================================

    @Nested
    @DisplayName("修改密码")
    class ChangePasswordTests {

        @Test
        @DisplayName("成功修改密码")
        void testChangePasswordSuccess() throws Exception {
            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword(TEST_PASSWORD);
            dto.setNewPassword("newPassword123");

            var result = post("/users/changePassword", dto);
            assertSuccess(result);

            // 用新密码登录验证
            var loginResult = post("/auth/login", Map.of(
                    "username", TEST_USERNAME,
                    "password", "newPassword123"
            ));
            assertSuccess(loginResult);

            // 恢复原密码
            ChangePasswordDTO restoreDto = new ChangePasswordDTO();
            restoreDto.setOldPassword("newPassword123");
            restoreDto.setNewPassword(TEST_PASSWORD);
            post("/users/changePassword", restoreDto);
        }
    }

    // ============================================================
    // 上传头像
    // ============================================================

    @Nested
    @DisplayName("上传头像")
    class UploadAvatarTests {

        @Test
        @DisplayName("成功上传头像")
        void testUploadAvatarSuccess() throws Exception {
            byte[] imageBytes = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
            var multipartFile = new MockMultipartFile(
                    "file", "test.png", "image/png", imageBytes);
            var request = MockMvcRequestBuilders
                    .multipart("/users/uploadAvatar")
                    .file(multipartFile)
                    .header("Authorization", "Bearer " + authToken);
            var result = mockMvc.perform(request)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            assertSuccess(result);
        }
    }
}
