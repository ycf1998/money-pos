package com.money.mail.service;

import cn.hutool.core.util.StrUtil;
import com.money.mail.config.ExtMailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;

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
    public boolean sendSimpleMail(String to, String subject, String content) {
        try {
            log.info("发送普通文本邮件：{},{},{}", to, subject, content);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAlias());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("发送普通文本邮件成功！");
        } catch (Exception e) {
            log.error("发送普通文本邮件失败：", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean sendSimpleMail(String to, String subject, String content, String... cc) {
        try {
            log.info("批量发送普通文本邮件：{},{},{}", to, subject, content);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAlias());
            message.setTo(to);
            message.setCc(cc);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("批量发送普通文本邮件成功！");
        } catch (Exception e) {
            log.error("批量发送普通文本邮件失败：", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean sendHtmlMail(String to, String subject, String content) {
        try {
            log.info("发送Html邮件：{},{},{}", to, subject, content);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(fromAlias());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            log.info("发送Html邮件成功！");
        } catch (Exception e) {
            log.error("发送Html邮件失败：", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean sendAttachmentsMail(String to, String subject, String content, String filePath) {
        try {
            log.info("发送带附件的邮件：{},{},{},{}", to, subject, content, filePath);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(fromAlias());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            // 多附件只要传入文件数组，一个一个addAttachment就可以了
            FileSystemResource file = new FileSystemResource(new File(filePath)); // 获取文件名
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            log.info("发送带附件的邮件成功！");
        } catch (Exception e) {
            log.error("发送带附件的邮件失败：", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {
        try {
            log.info("发送图片邮件：{},{},{},{},{}", to, subject, content, rscPath, rscId);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(fromAlias());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);
            mailSender.send(message);
            log.info("发送图片邮件成功！");
        } catch (Exception e) {
            log.error("发送图片邮件失败：", e);
            return false;
        }
        return true;
    }

}