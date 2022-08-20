package com.money.config;

import com.money.mb.config.Operator;
import com.money.security.SecurityGuard;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@MapperScan(basePackages = "com.money.mapper")
public class MybatisConfig {

    /**
     * 操作人
     *
     * @return {@link Operator}
     */
    @Primary
    @Bean
    public Operator operator() {
        return () -> SecurityGuard.isLoggedState() ? SecurityGuard.getRbacUser().getUsername() : "";
    }
}