package com.money.mail.service;

import com.money.mail.domian.MailRequest;

/**
 * 邮件服务
 *
 * @author : money
 * @since : 1.0.0
 */
public interface MailService {

    /**
     * 发送邮件
     *
     * @param mailRequest 邮件请求
     * @return boolean
     */
    boolean send(MailRequest mailRequest);

}