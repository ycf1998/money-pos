package com.money.mail.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.money.mail.config.ExtMailProperties;
import com.money.mail.domian.MailRequest;
import com.money.mail.interceptor.PostmanInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 邮件服务impl
 * @createTime : 2022-02-21 11:26:16
 */
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final ExtMailProperties mailProperties;
    private final JavaMailSender mailSender;

    /**
     * 发件人别名
     *
     * @return {@link String}
     */
    private String fromAlias() {
        String fromAlias = mailProperties.getFromAlias();
        if (StrUtil.isNotBlank(fromAlias)) {
            return new String((fromAlias + " <" + mailProperties.getUsername() + ">")
                    .getBytes(StandardCharsets.UTF_8));
        }
        return mailProperties.getUsername();
    }

    @Override
    public boolean send(MailRequest mailRequest) {
        List<PostmanInterceptor> postmanInterceptors = mailRequest.getPostmanInterceptors();
        try {
            log.info("发送邮件 {}", mailRequest);
            postmanInterceptors.forEach(p -> p.beforeSending(mailRequest));

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAlias());
            helper.setTo(mailRequest.getTo());
            helper.setSubject(mailRequest.getSubject());
            helper.setText(mailRequest.getContent(), mailRequest.isHtml());
            // 装填附件
            for (Map.Entry<String, File> fileNameWithFile : mailRequest.getAttachments().entrySet()) {
                File file = fileNameWithFile.getValue();
                String fileName = fileNameWithFile.getKey() + "." + FileUtil.getSuffix(file);
                helper.addAttachment(fileName, new FileSystemResource(file));
            }
            mailSender.send(message);

            postmanInterceptors.forEach(p -> p.sendSucceeded(mailRequest));
        } catch (MailException | MessagingException e) {
            log.error("发送邮件失败 ", e);
            postmanInterceptors.forEach(p -> p.sendFailed(mailRequest, e));
            return false;
        }
        return true;
    }

}