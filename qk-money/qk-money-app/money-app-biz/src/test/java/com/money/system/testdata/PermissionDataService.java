package com.money.system.testdata;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.SysPermission;
import com.money.mapper.SysPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限测试数据服务
 */
@Component
@Scope("prototype")
public class PermissionDataService {

    @Autowired
    private SysPermissionMapper permissionMapper;

    // 记录的测试数据 ID
    private final List<Long> createdPermissionIds = new ArrayList<>();

    /**
     * 创建测试权限（按钮类型）
     */
    public SysPermission createPermission(String permissionName, Long parentId, String permission) {
        SysPermission perm = new SysPermission();
        perm.setPermissionName(permissionName);
        perm.setPermissionType("BUTTON");
        perm.setParentId(parentId);
        perm.setIcon("icon");
        perm.setPermission(permission);
        perm.setRouterPath("");
        perm.setIframe(false);
        perm.setHidden(false);
        perm.setSort(1);
        perm.setTenantId(0L);
        permissionMapper.insert(perm);
        createdPermissionIds.add(perm.getId());
        return perm;
    }

    /**
     * 创建测试权限（目录类型）
     */
    public SysPermission createDir(String permissionName, Long parentId, String routerPath) {
        SysPermission perm = new SysPermission();
        perm.setPermissionName(permissionName);
        perm.setPermissionType("DIR");
        perm.setParentId(parentId);
        perm.setIcon("folder");
        perm.setRouterPath(routerPath);
        perm.setIframe(false);
        perm.setHidden(false);
        perm.setSort(1);
        perm.setTenantId(0L);
        permissionMapper.insert(perm);
        createdPermissionIds.add(perm.getId());
        return perm;
    }

    /**
     * 清理创建的测试数据
     */
    public void cleanup() {
        try {
            // 1. 删除权限（记录的 ID）
            if (!createdPermissionIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(permissionMapper)
                        .in(SysPermission::getId, createdPermissionIds)
                        .remove();
            }

            // 2. 删除权限（兜底：清理 test_ 前缀的权限）
            new LambdaUpdateChainWrapper<>(permissionMapper)
                    .likeRight(SysPermission::getPermissionName, "test_")
                    .remove();
        } catch (Exception e) {
            System.err.println("[PermissionDataService] 清理测试数据失败：" + e.getMessage());
        } finally {
            createdPermissionIds.clear();
        }
    }
}
