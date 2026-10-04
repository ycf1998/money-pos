package com.money.system.testdata;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.SysRole;
import com.money.entity.SysRolePermissionRelation;
import com.money.entity.SysUserRoleRelation;
import com.money.mapper.SysRoleMapper;
import com.money.mapper.SysRolePermissionRelationMapper;
import com.money.mapper.SysUserRoleRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色测试数据服务 - 负责角色数据的创建和清理
 */
@Component
@Scope("prototype")
public class RoleDataService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRolePermissionRelationMapper rolePermissionRelationMapper;

    @Autowired
    private SysUserRoleRelationMapper roleRelationMapper;

    // 记录的测试角色 ID
    private final List<Long> createdRoleIds = new ArrayList<>();

    // 测试角色默认配置
    private static final Long TEST_TENANT = 100L;

    /**
     * 创建测试角色（自动记录 ID）
     */
    public SysRole createRole(String roleCode, String roleName, int level) {
        SysRole role = new SysRole();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setLevel(level);
        role.setEnabled(true);
        role.setTenantId(TEST_TENANT);
        roleMapper.insert(role);
        createdRoleIds.add(role.getId());
        return role;
    }

    /**
     * 给用户分配角色
     */
    public void assignRole(Long userId, Long roleId) {
        SysUserRoleRelation relation = new SysUserRoleRelation();
        relation.setUserId(userId);
        relation.setRoleId(roleId);
        roleRelationMapper.insert(relation);
    }

    /**
     * 清理创建的测试角色数据
     * 
     * <p>清理策略：
     * 1. 先清理记录的角色 ID（精确清理）
     * 2. 再清理所有 TEST_ 前缀的残留数据（兜底清理）
     * </p>
     */
    public void cleanup() {
        try {
            // 1. 先删除角色 - 权限关联（记录的角色 ID）
            if (!createdRoleIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(rolePermissionRelationMapper)
                        .in(SysRolePermissionRelation::getRoleId, createdRoleIds)
                        .remove();
            }
            
            // 2. 删除角色 - 权限关联（兜底：清理所有 TEST_ 前缀的角色关联）
            new LambdaUpdateChainWrapper<>(rolePermissionRelationMapper)
                    .inSql(SysRolePermissionRelation::getRoleId,
                            "SELECT id FROM sys_role WHERE role_code LIKE 'TEST_%'")
                    .remove();

            // 3. 删除用户 - 角色关联（兜底：清理所有 TEST_ 前缀的角色关联）
            new LambdaUpdateChainWrapper<>(roleRelationMapper)
                    .inSql(SysUserRoleRelation::getRoleId,
                            "SELECT id FROM sys_role WHERE role_code LIKE 'TEST_%'")
                    .remove();

            // 4. 删除角色（记录的角色 ID）
            if (!createdRoleIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(roleMapper)
                        .in(SysRole::getId, createdRoleIds)
                        .remove();
            }
            
            // 5. 删除角色（兜底：清理所有 TEST_ 前缀的角色）
            new LambdaUpdateChainWrapper<>(roleMapper)
                    .likeRight(SysRole::getRoleCode, "TEST_")
                    .remove();
        } catch (Exception e) {
            System.err.println("[RoleDataService] 清理测试角色失败：" + e.getMessage());
        } finally {
            createdRoleIds.clear();
        }
    }
}
