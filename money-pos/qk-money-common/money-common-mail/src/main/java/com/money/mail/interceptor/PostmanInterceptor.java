package com.money.mail.interceptor;

import com.money.mail.domian.MailRequest;

/**
 * 邮差拦截器
 *
 * @author : money
 * @since : 1.0.0
 */
public interface PostmanInterceptor {

    /**
     * 在发送之前
     *
     * @param mailRequest 邮件请求
     */
    void beforeSending(MailRequest mailRequest);

    /**
     * 发送成功了
     */
    void sendSucceeded(MailRequest mailRequest);

    /**
     * 发送失败了
     *
     * @param mailRequest 邮件请求
     * @param e           MailException | MessagingException
     */
    void sendFailed(MailRequest mailRequest, Exception e);
}
