package com.money.system.testdata;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.entity.SysUser;
import com.money.entity.SysUserRoleRelation;
import com.money.mapper.SysUserMapper;
import com.money.mapper.SysUserRoleRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 用户测试数据服务 - 负责用户数据的创建和清理
 */
@Component
@Scope("prototype")
public class UserDataService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleRelationMapper roleRelationMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 记录的测试用户 ID
    private final List<Long> createdUserIds = new ArrayList<>();

    // 测试账号默认配置
    private static final String TEST_PASSWORD = "123456";
    private static final Long TEST_TENANT = 100L;

    /**
     * 创建测试用户（自动记录 ID，支持自定义）
     */
    public SysUser createUser(String suffix, Consumer<SysUser> customizer) {
        SysUser user = new SysUser();
        user.setUsername("test_" + suffix + "_" + System.currentTimeMillis());
        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        user.setNickname("测试用户");
        user.setPhone("13800000000");
        user.setEnabled(true);
        user.setTenantId(TEST_TENANT);

        if (customizer != null) {
            customizer.accept(user);
        }

        userMapper.insert(user);
        createdUserIds.add(user.getId());
        return user;
    }

    /**
     * 创建测试用户（默认配置）
     */
    public SysUser createUser(String suffix) {
        return createUser(suffix, null);
    }

    /**
     * 清理创建的测试用户数据
     * 
     * <p>清理策略：
     * 1. 先清理记录的用户 ID（精确清理）
     * 2. 再清理所有 test_ 前缀的残留数据（兜底清理），但保留 test_api（测试账号）
     * </p>
     */
    public void cleanup() {
        try {
            // 1. 先删除用户 - 角色关联（记录的用户 ID）
            if (!createdUserIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(roleRelationMapper)
                        .in(SysUserRoleRelation::getUserId, createdUserIds)
                        .remove();
            }
            
            // 2. 删除用户 - 角色关联（兜底：清理所有 test_ 前缀的用户关联，但保留 test_api）
            new LambdaUpdateChainWrapper<>(roleRelationMapper)
                    .inSql(SysUserRoleRelation::getUserId,
                            "SELECT id FROM sys_user WHERE username LIKE 'test_%' AND username != 'test_api'")
                    .remove();

            // 3. 删除用户（记录的用户 ID）
            if (!createdUserIds.isEmpty()) {
                new LambdaUpdateChainWrapper<>(userMapper)
                        .in(SysUser::getId, createdUserIds)
                        .remove();
            }
            
            // 4. 删除用户（兜底：清理所有 test_ 前缀的用户，但保留 test_api）
            new LambdaUpdateChainWrapper<>(userMapper)
                    .likeRight(SysUser::getUsername, "test_")
                    .ne(SysUser::getUsername, "test_api")
                    .remove();
        } catch (Exception e) {
            System.err.println("[UserDataService] 清理测试用户失败：" + e.getMessage());
        } finally {
            createdUserIds.clear();
        }
    }
}
