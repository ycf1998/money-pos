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
 * @author : money
 * @version : 1.0.0
 * @description : 租户配置
 * @createTime : 2022-01-01 20:52:06
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TenantProperties.class)
public class TenantConfiguration {

    public TenantConfiguration(TenantProperties tenantProperties, MybatisPlusInterceptor interceptor) {
        if (tenantProperties.isEnabled()) {
            // 多租户插件得在分页前，不然会出现分页查询total有条数（没过滤租户前），但实际没有（过滤租户后records没了）。
            List<InnerInterceptor> interceptors = new ArrayList<>();
            // 添加mybatis plus 多租户插件
            interceptors.add(new TenantLineInnerInterceptor(new TenantLineHandler() {

                @SneakyThrows
                @Override
                public Expression getTenantId() {
                    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    assert servletRequestAttributes != null;
                    String tenantId = servletRequestAttributes.getRequest().getHeader(tenantProperties.getHeader());
                    if (StrUtil.isBlank(tenantId)) {
                        tenantId = tenantProperties.getDefaultTenantId();
                    }
                    TenantContextHolder.setTenant(Long.valueOf(tenantId));
                    return new LongValue(tenantId);
                }

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
