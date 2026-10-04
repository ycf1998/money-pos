package com.money.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.money.base.ControllerTestBase;
import com.money.system.testdata.PermissionDataService;
import com.money.dto.SysPermissionDTO;
import com.money.entity.SysPermission;
import com.money.mapper.SysPermissionMapper;
import com.money.vo.SysPermissionVO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 权限管理接口测试
 */
@DisplayName("权限管理接口测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SysPermissionControllerTest extends ControllerTestBase {

    @Autowired
    private PermissionDataService permissionDataService;

    @Autowired
    private SysPermissionMapper permissionMapper;

    // 测试根节点权限 ID
    private Long testRootId = 0L;

    @BeforeEach
    void setUp() {
        // 获取一个根节点用于创建子权限
        SysPermission root = permissionMapper.selectOne(
                new LambdaQueryWrapper<SysPermission>()
                        .eq(SysPermission::getParentId, 0L)
                        .orderByAsc(SysPermission::getSort)
                        .last("LIMIT 1")
        );
        if (root != null) {
            testRootId = root.getId();
        }
    }

    @AfterEach
    void tearDown() {
        permissionDataService.cleanup();
    }

    // ============================================================
    // 查询权限
    // ============================================================

    @Nested
    @DisplayName("查询权限")
    class ListPermissionTests {

        @BeforeEach
        void setUp() {
            // 创建测试数据
            for (int i = 0; i < 3; i++) {
                permissionDataService.createPermission("test_permission_" + i, testRootId, "test:perm_" + i);
            }
        }

        @Test
        @DisplayName("成功查询权限列表")
        void testListPermissions() throws Exception {
            var result = get("/permissions", Map.of());
            assertSuccess(result);

            var list = getListData(result, SysPermissionVO.class);
            assertNotNull(list);
            // 验证返回的列表中包含测试数据（检查根节点的 children）
            boolean hasTestData = list.stream()
                    .anyMatch(p -> p.getChildren() != null && p.getChildren().stream()
                            .anyMatch(c -> c.getPermissionName() != null && c.getPermissionName().startsWith("test_")));
            assertTrue(hasTestData, "应该包含测试数据");
        }

        @Test
        @DisplayName("成功按权限名称搜索")
        void testListPermissionsByName() throws Exception {
            var result = get("/permissions", Map.of("condition", "test_permission_0"));
            assertSuccess(result);

            var list = getListData(result, SysPermissionVO.class);
            assertTrue(list.stream()
                    .anyMatch(p -> p.getPermissionName() != null && p.getPermissionName().contains("test_permission_0")));
        }

        @Test
        @DisplayName("成功按权限类型筛选")
        void testListPermissionsByType() throws Exception {
            var result = get("/permissions", Map.of("permissionType", "BUTTON"));
            assertSuccess(result);

            var list = getListData(result, SysPermissionVO.class);
            assertTrue(list.stream().allMatch(p -> "BUTTON".equals(p.getPermissionType())));
        }
    }

    // ============================================================
    // 添加权限
    // ============================================================

    @Nested
    @DisplayName("添加权限")
    class AddPermissionTests {

        @Test
        @DisplayName("成功添加权限")
        void testAddPermissionSuccess() throws Exception {
            SysPermissionDTO dto = new SysPermissionDTO();
            dto.setPermissionName("test_add_permission");
            dto.setPermissionType("BUTTON");
            dto.setParentId(testRootId);
            dto.setIcon("icon");
            dto.setPermission("test:add");
            dto.setHidden(false);
            dto.setSort(1);

            var result = post("/permissions", dto);
            assertSuccess(result);
        }

        @Test
        @DisplayName("成功添加目录权限")
        void testAddDirPermission() throws Exception {
            SysPermissionDTO dto = new SysPermissionDTO();
            dto.setPermissionName("test_dir_permission");
            dto.setPermissionType("DIR");
            dto.setParentId(testRootId);
            dto.setIcon("folder");
            dto.setRouterPath("test_dir");
            dto.setHidden(false);
            dto.setSort(1);

            var result = post("/permissions", dto);
            assertSuccess(result);
        }
    }

    // ============================================================
    // 修改权限
    // ============================================================

    @Nested
    @DisplayName("修改权限")
    class UpdatePermissionTests {

        private Long createdPermissionId = null;

        @BeforeEach
        void setUp() {
            // 创建测试权限
            SysPermission permission = permissionDataService.createPermission("test_update_permission", testRootId, "test:update");
            createdPermissionId = permission.getId();
        }

        @AfterEach
        void tearDown() {
            permissionDataService.cleanup();
        }

        @Test
        @DisplayName("成功修改权限")
        void testUpdatePermissionSuccess() throws Exception {
            SysPermissionDTO dto = new SysPermissionDTO();
            dto.setId(createdPermissionId);
            dto.setPermissionName("test_updated_permission");
            dto.setPermissionType("BUTTON");
            dto.setParentId(testRootId);
            dto.setIcon("new-icon");
            dto.setPermission("test:updated");
            dto.setHidden(false);
            dto.setSort(2);

            var result = put("/permissions", dto);
            assertSuccess(result);

            // 验证修改结果
            SysPermission updated = permissionMapper.selectById(createdPermissionId);
            assertNotNull(updated);
            assertEquals("test_updated_permission", updated.getPermissionName());
            assertEquals("new-icon", updated.getIcon());
        }
    }

    // ============================================================
    // 删除权限
    // ============================================================

    @Nested
    @DisplayName("删除权限")
    class DeletePermissionTests {

        @Test
        @DisplayName("成功删除权限")
        void testDeletePermissionSuccess() throws Exception {
            // 创建测试权限
            SysPermission permission = permissionDataService.createPermission("test_delete_permission", testRootId, "test:delete");

            var result = delete("/permissions", Set.of(permission.getId()));
            assertSuccess(result);

            assertNull(permissionMapper.selectById(permission.getId()));
        }

        @Test
        @DisplayName("成功批量删除权限")
        void testDeletePermissionsBatch() throws Exception {
            // 创建测试权限
            SysPermission permission1 = permissionDataService.createPermission("test_delete_batch_1", testRootId, "test:delete1");
            SysPermission permission2 = permissionDataService.createPermission("test_delete_batch_2", testRootId, "test:delete2");

            var result = delete("/permissions", Set.of(permission1.getId(), permission2.getId()));
            assertSuccess(result);

            assertNull(permissionMapper.selectById(permission1.getId()));
            assertNull(permissionMapper.selectById(permission2.getId()));
        }
    }
}
