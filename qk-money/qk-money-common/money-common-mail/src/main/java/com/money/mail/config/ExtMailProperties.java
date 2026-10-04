package com.money.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 邮件配置
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "money.mail")
public class ExtMailProperties {

    /**
     * 邮件服务器主机
     */
    private String host;

    /**
     * 邮件服务器端口
     */
    private int port = 25;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 默认编码
     */
    private Charset defaultEncoding = StandardCharsets.UTF_8;

    /**
     * 协议
     */
    private String protocol = "smtp";

    /**
     * 其他属性
     */
    private Properties properties = new Properties();

    /**
     * 发件人别名
     */
    private String fromAlias;
}
