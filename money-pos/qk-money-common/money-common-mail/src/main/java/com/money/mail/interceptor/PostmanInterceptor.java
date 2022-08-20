package com.money.mail.interceptor;

import com.money.mail.domian.MailRequest;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 邮差拦截器
 * @createTime : 2022-05-14 11:12:36
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
