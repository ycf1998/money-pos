package com.money.mail.service;

import com.money.mail.domian.MailRequest;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 邮件服务
 * @createTime : 2022-02-21 11:26:12
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