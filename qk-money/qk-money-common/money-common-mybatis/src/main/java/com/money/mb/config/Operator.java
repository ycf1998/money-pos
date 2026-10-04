package com.money.mb.config;

/**
 * 操作者
 *
 * @author : money
 * @since : 1.0.0
 */
@FunctionalInterface
public interface Operator {

    /**
     * 获取用户名
     *
     * @return {@link String }
     */
    String getUsername();

}
