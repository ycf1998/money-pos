# 测试用例覆盖文档

> 文档说明：记录每个 Controller 的接口测试覆盖情况，仅包含 Happy Path（成功场景）测试用例。

---

## 测试原则

**只测试 Happy Path** - 仅验证接口在正常情况下的功能，不测试异常场景。

| 测试 | 说明 |
|------|------|
| ✅ 测试 | 正常参数、正常流程、预期成功 |
| ❌ 不测试 | 参数校验、异常流程、错误处理 |

---

## 图例说明

| 状态 | 说明 |
|------|------|
| ✅ | 已完成 |
| ⏳ | 进行中 |
| ❌ | 未开始 |
| ⚠️ | 部分完成/需优化 |

---

## 文档索引

| 文档 | 接口数 | 已覆盖 | 覆盖率 | 状态 |
|------|--------|--------|--------|------|
| [SysRoleController.md](SysRoleController.md) | 6 | 6 | 100% | ✅ |
| [SysUserController.md](SysUserController.md) | 7 | 7 | 100% | ✅ |
| [SysAuthController.md](SysAuthController.md) | 5 | 5 | 100% | ✅ |
| [SysPermissionController.md](SysPermissionController.md) | 4 | 4 | 100% | ✅ |
| [SysDictController.md](SysDictController.md) | 8 | 8 | 100% | ✅ |
| **合计** | **30** | **30** | **100%** | **✅** |

---

## 总体统计

### 测试覆盖情况

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| Controller 总数 | 5 | 100% |
| 已完成 Controller | 5 | 100% |
| 接口总数 | 30 | 100% |
| 已覆盖接口 | 30 | 100% |
| 测试用例总数 | 40 | - |
| 通过用例数 | 40 | 100% |
| 禁用用例数 | 0 | 0% |

---

## 待办事项

| 优先级 | Controller | 任务 | 状态 |
|--------|------------|------|------|
| - | - | - | - |

---

## 附录：测试数据管理

### TestDataService 使用说明

测试数据统一通过 `TestDataService` 管理，遵循"谁创建谁清理"原则。

```java
@SpringBootTest
class MyTest extends ControllerTestBase {

    @Autowired
    private TestDataService testData;

    @AfterEach
    void tearDown() {
        testData.cleanup();  // 自动清理
    }

    @Test
    void testExample() {
        // 创建测试数据
        SysRole role = testData.createRole("TEST", "测试角色", 10);
        SysUser user = testData.createUser("test");
        testData.assignRole(user.getId(), role.getId());
        
        // 测试代码...
    }
}
```

### 清理策略

| 服务 | 清理范围 | 兜底策略 |
|------|----------|----------|
| `UserDataService` | 记录的用户 ID | 清理所有 `test_` 前缀用户（排除 `test_api`） |
| `RoleDataService` | 记录的角色 ID | 清理所有 `TEST_` 前缀角色 |

---

> 文档更新时间：2026-03-30  
> 最后更新人：AI Assistant
