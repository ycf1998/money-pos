package com.money.mail.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 邮件配置
 * @createTime : 2022-02-21 10:56:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "money.mail")
public class ExtMailProperties extends MailProperties {

    /**
     * 发件人别名
     */
    private String fromAlias;
}
