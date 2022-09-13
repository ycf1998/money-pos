package com.money.mb;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.money.mb.base.BaseEntity;
import lombok.Getter;

import java.io.File;
import java.util.*;

/**
 * @author : money
 * @version : 1.0.0
 * @description : mybatis plus 代码生成器
 * @createTime : 2022-01-01 16:36:00
 */
public class MybatisPlusGenerator {

    /**
     * url
     */
    private static final String url = "jdbc:mysql://127.0.0.1:3306/qk_money?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2b8&useSSL=false&allowPublicKeyRetrieval=true";
    /**
     * 用户名
     */
    private static final String username = "root";
    /**
     * 密码
     */
    private static final String password = "root";

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(url, username, password);

    public static void main(String[] args) {
        Answer answer = askAndAnswer();
        new AutoGenerator(DATA_SOURCE_CONFIG.build())
                // 全局配置
                .global(answer.globalConfig.build())
                // 包配置
                .packageInfo(answer.packageConfig.build())
                // 策略配置
                .strategy(answer.strategyConfig.build())
                // 注入配置
                .injection(answer.injectionConfig.build())
                // 模板配置
                .template(answer.templateConfig.build())
                // 执行
                .execute(new FreemarkerTemplateEngine());
    }

    public static Answer askAndAnswer() {
        List<String> yesOrNo = ListUtil.of("Y", "y", "N", "n");
        Answer answer = new Answer();
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入作者？(money)：");
        String s = StrUtil.blankToDefault(scanner.nextLine(), "money");
        answer.author(s);
        do {
            System.out.print("是覆盖已有文件[Y|N]？(N)：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "N");
            answer.fileOverride(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("是否开启swagger[Y|N]？(Y)：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "Y");
            answer.openSwagger(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("是否生成xml文件[Y|N]？(N)：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "N");
            answer.generateXml(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("是否继承BaseEntity[Y|N]？(Y)：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "Y");
            answer.extendBaseEntity(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("是否开启Controller @PreAuthorize权限控制注解[Y|N]？(Y)：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "Y");
            answer.openAuth(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("输入要生成的表，支持%模糊？(所有输入 %)：");
            s = scanner.nextLine();
            answer.targetTable(s);
        } while (StrUtil.isBlank(s));
        return answer;
    }

    @Getter
    public static class Answer {

        /**
         * 全局配置
         */
        private final GlobalConfig.Builder globalConfig = new GlobalConfig.Builder();

        /**
         * 包配置
         */
        private final PackageConfig.Builder packageConfig = new PackageConfig.Builder();

        /**
         * 模板配置
         */
        private final TemplateConfig.Builder templateConfig = new TemplateConfig.Builder();

        /**
         * 注入配置
         */
        private final InjectionConfig.Builder injectionConfig = new InjectionConfig.Builder();

        /**
         * 策略配置
         */
        private final StrategyConfig.Builder strategyConfig = new StrategyConfig.Builder();

        public Answer() {
            Map<String, String> DTOAndVOTemplateMap = new HashMap<>();
            globalConfig.disableOpenDir().outputDir(new File("qk-money-app/money-app-biz/src/main/java/").getAbsolutePath());
            packageConfig.parent("com.money").other("dto")
                    .pathInfo(new HashMap<OutputFile, String>() {
                        private static final long serialVersionUID = 2846316720798527526L;

                        {
                            put(OutputFile.entity, new File("qk-money-app/money-app-api/src/main/java/com/money/entity").getAbsolutePath());
                            put(OutputFile.mapperXml, new File("qk-money-app/money-app-biz/src/main/resources/mapper").getAbsolutePath());
                            put(OutputFile.other, new File("qk-money-app/money-app-api/src/main/java/com/money/dto").getAbsolutePath());
                        }
                    });
            injectionConfig.customMap(Collections.singletonMap("preAuthorize", false))
                    .beforeOutputFile((tableInfo, objectMap) -> {
                        DTOAndVOTemplateMap.put(tableInfo.getEntityName() + "QueryDTO.java", "/templates/queryDto.java.ftl");
                        DTOAndVOTemplateMap.put(tableInfo.getEntityName() + "DTO.java", "/templates/dto.java.ftl");
                        DTOAndVOTemplateMap.put(tableInfo.getEntityName() + "VO.java", "/templates/vo.java.ftl");
                    })
                    .customFile(DTOAndVOTemplateMap);
            strategyConfig.serviceBuilder().formatServiceFileName("%sService");
            strategyConfig.controllerBuilder().enableRestStyle();
        }

        /**
         * 作者
         *
         * @param author 作者
         */
        public void author(String author) {
            globalConfig.author(author);
        }

        /**
         * 开启swagger
         *
         * @param yesOrNo 是或否
         */
        public void openSwagger(String yesOrNo) {
            if ("Y".equalsIgnoreCase(yesOrNo)) {
                globalConfig.enableSwagger();
            }
        }

        /**
         * 文件覆盖
         *
         * @param yesOrNo 是或否
         */
        public void fileOverride(String yesOrNo) {
            if ("Y".equalsIgnoreCase(yesOrNo)) {
                globalConfig.fileOverride();
            }
        }

        /**
         * 生成xml
         *
         * @param yesOrNo 是或否
         */
        public void generateXml(String yesOrNo) {
            if ("N".equalsIgnoreCase(yesOrNo)) {
                templateConfig.disable(TemplateType.XML);
            } else {
                strategyConfig.mapperBuilder().enableBaseColumnList().enableBaseResultMap();
            }
        }

        /**
         * 开放身份验证
         * 生成xml
         *
         * @param yesOrNo 是或否
         */
        public void openAuth(String yesOrNo) {
            if ("Y".equalsIgnoreCase(yesOrNo)) {
                injectionConfig.customMap(Collections.singletonMap("preAuthorize", true));
            }
        }

        /**
         * 目标表
         *
         * @param table 表格
         */
        public void targetTable(String table) {
            if ("%".equals(table)) {
                strategyConfig.addInclude("all");
                return;
            }
            SqlLike like = null;
            if (table.startsWith("%")) {
                like = SqlLike.LEFT;
            }
            if (table.endsWith("%")) {
                like = like == SqlLike.LEFT ? SqlLike.DEFAULT : SqlLike.RIGHT;
            }
            if (like == null) {
                strategyConfig.addInclude(table);
            } else {
                strategyConfig.likeTable(new LikeTable(table, like));
            }
        }

        /**
         * 扩展基础实体
         *
         * @param yesOrNo 是或否
         */
        public void extendBaseEntity(String yesOrNo) {
            strategyConfig.entityBuilder().enableLombok().disableSerialVersionUID();
            if ("Y".equalsIgnoreCase(yesOrNo)) {
                strategyConfig.entityBuilder().superClass(BaseEntity.class)
                        .addIgnoreColumns("create_time", "create_by", "update_by", "update_time", "id");
            }
        }
    }
}
