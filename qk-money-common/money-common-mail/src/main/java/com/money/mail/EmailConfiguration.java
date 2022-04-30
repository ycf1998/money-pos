package com.money.mail;

import com.money.mail.config.ExtMailProperties;
import com.money.mail.service.MailService;
import com.money.mail.service.MailServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 邮件配置
 * @createTime : 2022-02-21 11:49:52
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
        javaMailSender.setDefaultEncoding(properties.getDefaultEncoding().displayName());
        javaMailSender.setUsername(properties.getUsername());
        javaMailSender.setPassword(properties.getPassword());
        Properties p = new Properties();
        properties.getProperties().forEach(p::setProperty);
        javaMailSender.setJavaMailProperties(p);
        return new MailServiceImpl(properties, javaMailSender);
    }
}
