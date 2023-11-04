package com.money.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    /**
     * 项目名
     */
    @Value("${spring.application.name:未命名}")
    private String projectName;
    /**
     * 扫描包
     */
    private String scanPackage = "com.money";
    /**
     * 标题
     */
    private String title = "";

    /**
     * 版本
     */
    private String version = "";

    /**
     * 描述
     */
    private String description = "";


    /**
     * auth头
     */
    private String authHeader;

    /**
     * 联系人
     */
    private Contact contact = new Contact();

    /**
     * 许可证
     */
    private License license = new License();

    /**
     * 外部文档
     */
    private ExternalDocumentation externalDocumentation = new ExternalDocumentation();


    @Getter
    @Setter
    static class Contact {
        /**
         * 名字
         */
        private String name = "";
        /**
         * url
         */
        private String url = "";
        /**
         * 电子邮件
         */
        private String email = "";
    }

    @Getter
    @Setter
    static class License {
        /**
         * 名称
         */
        private String name = "";

        /**
         * url
         */
        private String url = "";
    }

    @Getter
    @Setter
    static  class ExternalDocumentation {
        /**
         * url
         */
        private String url = "";

        /**
         * 描述
         */
        private String description = "";
    }
}
