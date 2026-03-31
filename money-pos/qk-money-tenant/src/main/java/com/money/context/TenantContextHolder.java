package com.money.context;

/**
 * 租户上下文持有者
 * <p>基于 {@link ThreadLocal} 实现租户 ID 的线程隔离存储。</p>
 *
 * @author money
 * @since 1.0.0
 */
public class TenantContextHolder {

    /**
     * 租户上下文线程本地变量
     */
    private static final ThreadLocal<Long> tenantContext = new ThreadLocal<>();

    /**
     * 设置当前线程的租户 ID
     *
     * @param tenantId 租户标识
     */
    public static void setTenant(Long tenantId) {
        tenantContext.set(tenantId);
    }

    /**
     * 获取当前线程的租户 ID
     *
     * @return 租户标识，未设置返回 {@code null}
     */
    public static Long getTenant() {
        return tenantContext.get();
    }

}
