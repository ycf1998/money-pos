package com.money.mb.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/**
 * @author : money
 * @version : 1.0.0
 * @description : mybatis plus审计字段自动填充
 * @createTime : 2022-01-01 17:10:51
 */
@RequiredArgsConstructor
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    private final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    private final Operator operator;

    /**
     * 主键生成策略
     *
     * @return {@link IdentifierGenerator}
     */
    @Bean
    public IdentifierGenerator idGenerator() {
        return entity -> snowflake.nextId();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createBy", operator::getUsername, String.class);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, "updateBy", operator::getUsername, String.class);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateBy", operator::getUsername, String.class);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
