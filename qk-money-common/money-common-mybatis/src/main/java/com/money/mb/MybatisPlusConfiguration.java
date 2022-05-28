package com.money.mb;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.money.mb.config.MybatisPlusMetaObjectHandler;
import com.money.mb.config.Operator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class MybatisPlusConfiguration {

    /**
     * 操作者
     *
     * @return {@link Operator}
     */
    @Bean
    @ConditionalOnMissingBean(Operator.class)
    public Operator operator() {
        return () -> "";
    }

    /**
     * 分页插件配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Nullable List<InnerInterceptor> interceptors) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (CollUtil.isNotEmpty(interceptors)) {
            interceptors.forEach(interceptor::addInnerInterceptor);
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * mybatis+元对象处理器
     *
     * @param operator 操作符
     * @return {@link MybatisPlusMetaObjectHandler}
     */
    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler(Operator operator) {
        return new MybatisPlusMetaObjectHandler(operator);
    }
}
