package com.money.system.controller;

import com.money.base.ControllerTestBase;
import com.money.system.testdata.RoleDataService;
import com.money.dto.SysRoleDTO;
import com.money.entity.SysRole;
import com.money.entity.SysRolePermissionRelation;
import com.money.mapper.SysRoleMapper;
import com.money.mapper.SysRolePermissionRelationMapper;
import com.money.vo.SysRoleVO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 角色管理接口测试
 */
@DisplayName("角色管理接口测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SysRoleControllerTest extends ControllerTestBase {

    @Autowired
    private RoleDataService roleDataService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRolePermissionRelationMapper rolePermissionMapper;

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
        roleDataService.cleanup();
    }

    /**
     * 所有测试结束后，清理全局数据
     */
    @AfterAll
    void cleanupGlobal() {
        roleDataService.cleanup();
    }

    // ============================================================
    // 分页查询角色
    // ============================================================

    @Nested
    @DisplayName("分页查询角色")
    class ListRolesTests {

        @BeforeEach
        void setUp() {
            // 创建 5 个测试角色
            for (int i = 0; i < 5; i++) {
                roleDataService.createRole("TEST_page_" + i, "测试角色_page_" + i, 10);
            }
        }

        @Test
        @DisplayName("成功分页查询角色列表")
        void testListRoles() throws Exception {
            var result = get("/roles", Map.of("page", "1", "size", "2"));
            assertSuccess(result);

            var pageData = getPageData(result, SysRoleVO.class);
            assertNotNull(pageData);
            assertEquals(2, pageData.getRecords().size());
            assertTrue(pageData.getTotal() >= 5);
        }

        @Test
        @DisplayName("成功按角色编码搜索角色")
        void testListRolesByRoleCode() throws Exception {
            var result = get("/roles", Map.of(
                    "page", "1",
                    "size", "10",
                    "roleCode", "TEST_page_0"
            ));
            assertSuccess(result);

            var pageData = getPageData(result, SysRoleVO.class);
            assertTrue(pageData.getRecords().stream()
                    .anyMatch(r -> r.getRoleCode().contains("TEST_page_0")));
        }

        @Test
        @DisplayName("成功按启用状态筛选角色")
        void testListRolesByEnabled() throws Exception {
            var result = get("/roles", Map.of(
                    "page", "1",
                    "size", "10",
                    "enabled", "true"
            ));
            assertSuccess(result);

            var pageData = getPageData(result, SysRoleVO.class);
            assertTrue(pageData.getRecords().stream().allMatch(SysRoleVO::getEnabled));
        }
    }

    // ============================================================
    // 添加角色
    // ============================================================

    @Nested
    @DisplayName("添加角色")
    class AddRoleTests {

        @Test
        @DisplayName("成功添加角色")
        void testAddRoleSuccess() throws Exception {
            SysRoleDTO dto = new SysRoleDTO();
            dto.setRoleCode("TEST_ADD_" + System.currentTimeMillis());
            dto.setRoleName("测试添加角色");
            dto.setLevel(10);
            dto.setDescription("测试添加角色描述");
            dto.setEnabled(true);

            var result = post("/roles", dto);
            assertSuccess(result);
            // 注意：通过 API 创建的角色，TestDataService 无法清理
            // 依赖 RoleDataService 的兜底清理（清理所有 TEST_ 前缀的角色）
        }
    }

    // ============================================================
    // 修改角色
    // ============================================================

    @Nested
    @DisplayName("修改角色")
    class UpdateRoleTests {

        private SysRole testRole;

        @BeforeEach
        void setUp() {
            testRole = roleDataService.createRole("TEST_update", "测试角色_update", 10);
        }

        @Test
        @DisplayName("成功修改角色")
        void testUpdateRoleSuccess() throws Exception {
            SysRoleDTO updateDto = new SysRoleDTO();
            updateDto.setId(testRole.getId());
            updateDto.setRoleCode("TEST_UPDATE_NEW_" + System.currentTimeMillis());
            updateDto.setRoleName("更新后角色名称");
            updateDto.setLevel(10);
            updateDto.setDescription("更新后描述");
            updateDto.setEnabled(true);

            var result = put("/roles", updateDto);
            assertSuccess(result);
        }
    }

    // ============================================================
    // 删除角色
    // ============================================================

    @Nested
    @DisplayName("删除角色")
    class DeleteRoleTests {

        @Test
        @DisplayName("成功删除角色")
        void testDeleteRoleSuccess() throws Exception {
            SysRole role = roleDataService.createRole("TEST_delete", "测试角色_delete", 10);

            var result = delete("/roles", Set.of(role.getId()));
            assertSuccess(result);

            assertNull(sysRoleMapper.selectById(role.getId()));
        }

        @Test
        @DisplayName("成功批量删除角色")
        void testDeleteRolesBatch() throws Exception {
            SysRole role1 = roleDataService.createRole("TEST_batch1", "测试角色_batch1", 10);
            SysRole role2 = roleDataService.createRole("TEST_batch2", "测试角色_batch2", 10);

            var result = delete("/roles", Set.of(role1.getId(), role2.getId()));
            assertSuccess(result);

            assertNull(sysRoleMapper.selectById(role1.getId()));
            assertNull(sysRoleMapper.selectById(role2.getId()));
        }
    }

    // ============================================================
    // 配置权限
    // ============================================================

    @Nested
    @DisplayName("配置权限")
    class ConfigurePermissionsTests {

        private SysRole testRole;

        @BeforeEach
        void setUp() {
            testRole = roleDataService.createRole("TEST_permission", "测试角色_permission", 10);
        }

        @Test
        @DisplayName("成功配置角色权限")
        void testConfigurePermissionsSuccess() throws Exception {
            Set<Long> permissions = Set.of(1L, 2L, 3L);

            var result = post("/roles/" + testRole.getId() + "/permission", permissions);
            assertSuccess(result);

            // 验证权限已配置
            var savedPermissions = rolePermissionMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysRolePermissionRelation>()
                            .eq(SysRolePermissionRelation::getRoleId, testRole.getId())
            );
            assertEquals(3, savedPermissions.size());
        }

        @Test
        @DisplayName("成功清空角色权限")
        void testConfigurePermissionsEmpty() throws Exception {
            // 先添加一些权限
            Set<Long> initialPermissions = Set.of(1L, 2L);
            post("/roles/" + testRole.getId() + "/permission", initialPermissions);

            // 清空权限
            Set<Long> emptyPermissions = Set.of();
            var result = post("/roles/" + testRole.getId() + "/permission", emptyPermissions);
            assertSuccess(result);

            // 验证权限已清空
            var savedPermissions = rolePermissionMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysRolePermissionRelation>()
                            .eq(SysRolePermissionRelation::getRoleId, testRole.getId())
            );
            assertTrue(savedPermissions.isEmpty());
        }
    }
}
