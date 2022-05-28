package com.money.mail.domian;

import cn.hutool.core.util.StrUtil;
import com.money.mail.interceptor.PostmanInterceptor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 邮件请求
 * @createTime : 2022-05-14 11:24:23
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class MailRequest {

    /**
     * 收件人
     */
    private final String to;

    /**
     * 主题
     */
    private final String subject;

    /**
     * 内容
     */
    private final String content;

    /**
     * 是否解析为html
     */
    private boolean html;

    /**
     * 抄送，可选
     */
    private List<String> cc;

    /**
     * 附件列表 <附件名，文件>，可选
     */
    private Map<String, File> attachments = new HashMap<>();

    /**
     * 邮差拦截器，可选
     */
    private List<PostmanInterceptor> postmanInterceptors = new ArrayList<>();

    /**
     * 添加附件
     *
     * @param fileName 文件名称
     * @param file     文件
     */
    public MailRequest addAttachment(String fileName, File file) {
        attachments.put(fileName, file);
        return this;
    }

    /**
     * 添加邮差拦截器
     *
     * @param postmanInterceptor 邮差拦截器
     */
    public MailRequest addPostmanInterceptor(PostmanInterceptor postmanInterceptor) {
        this.postmanInterceptors.add(postmanInterceptor);
        return this;
    }

    @Override
    public String toString() {
        return StrUtil.format("-> 【主题】 {} 【发件人】 {} 【抄送】 {} ", subject, to, cc);
    }
}
