package com.money;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.money.context.TenantContextHolder;
import com.money.properties.TenantProperties;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置类
 * <p>基于 MyBatis-Plus 的 {@link TenantLineInnerInterceptor} 实现多租户数据隔离。</p>
 *
 * @author money
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TenantProperties.class)
public class TenantConfiguration {

    /**
     * 初始化多租户拦截器
     * <p>多租户插件在分页前执行，确保先过滤租户再分页。</p>
     *
     * @param tenantProperties 多租户配置属性
     * @param interceptor      MyBatis-Plus 拦截器
     */
    public TenantConfiguration(TenantProperties tenantProperties, MybatisPlusInterceptor interceptor) {
        if (tenantProperties.isEnabled()) {
            // 多租户插件得在分页前，不然会出现分页查询 total 有条数（没过滤租户前），但实际没有（过滤租户后 records 没了）。
            List<InnerInterceptor> interceptors = new ArrayList<>();
            // 添加 mybatis plus 多租户插件
            interceptors.add(new TenantLineInnerInterceptor(new TenantLineHandler() {

                /**
                 * 获取租户 ID 表达式
                 * <p>从请求头获取租户 ID，未获取到则使用默认值。</p>
                 *
                 * @return 租户 ID 的 SQL 表达式
                 */
                @SneakyThrows
                @Override
                public Expression getTenantId() {
                    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    String tenantId = servletRequestAttributes != null
                        ? servletRequestAttributes.getRequest().getHeader(tenantProperties.getHeader())
                        : null;
                    if (StrUtil.isBlank(tenantId)) {
                        tenantId = tenantProperties.getDefaultTenantId();
                    }
                    TenantContextHolder.setTenant(Long.valueOf(tenantId));
                    return new LongValue(tenantId);
                }

                /**
                 * 判断是否忽略指定表的租户隔离
                 *
                 * @param tableName 表名
                 * @return {@code true} 表示忽略租户隔离
                 */
                @Override
                public boolean ignoreTable(String tableName) {
                    return tenantProperties.getIgnoreTable().contains(tableName);
                }
            }));

            interceptors.addAll(interceptor.getInterceptors());
            interceptor.setInterceptors(interceptors);
        }
    }
}
