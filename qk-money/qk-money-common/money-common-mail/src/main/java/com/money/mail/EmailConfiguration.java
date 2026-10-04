package com.money.mail;

import com.money.mail.config.ExtMailProperties;
import com.money.mail.service.MailService;
import com.money.mail.service.MailServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 邮件配置类
 *
 * @author : money
 * @since : 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExtMailProperties.class)
public class EmailConfiguration {

    /**
     * 邮件服务
     *
     * @param properties 属性
     * @return {@link MailService}
     */
    @Bean
    public MailService mailService(ExtMailProperties properties) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(properties.getHost());
        javaMailSender.setPort(properties.getPort());
        javaMailSender.setProtocol(properties.getProtocol());
        javaMailSender.setDefaultEncoding(properties.getDefaultEncoding().name());
        javaMailSender.setUsername(properties.getUsername());
        javaMailSender.setPassword(properties.getPassword());
        javaMailSender.setJavaMailProperties(properties.getProperties());
        return new MailServiceImpl(properties, javaMailSender);
    }
}
