package com.money.mb;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.money.mb.base.BaseEntity;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    public static void main(String[] args) {
        Answer answer = askAndAnswer();
        new AutoGenerator(new DataSourceConfig.Builder(url, username, password).build())
                // 全局配置
                .global(answer.globalConfig.build())
                // 包配置
                .packageInfo(answer.packageConfig.build())
                // 策略配置
                .strategy(answer.strategyConfig.build())
                // 注入配置
                .injection(answer.injectionConfig.build())
                // 执行
                .execute(new FreemarkerTemplateEngine());
    }

    public static Answer askAndAnswer() {
        List<String> yesOrNo = ListUtil.of("Y", "y", "N", "n");
        Answer answer = new Answer();
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入作者？（money）：");
        String s = StrUtil.blankToDefault(scanner.nextLine(), "money");
        answer.author(s);
        do {
            System.out.print("是覆盖已有文件 [Y|N]？（N）：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "N");
            answer.fileOverride(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("是否开启 Swagger [Y|N]？（N）：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "N");
            answer.openSwagger(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("是否生成 XML 文件 [Y|N]？（N）：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "N");
            answer.generateXml(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("是否继承 BaseEntity [Y|N]？（Y）：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "Y");
            answer.extendBaseEntity(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("是否开启 Controller @PreAuthorize 权限控制注解 [Y|N]？（Y）：");
            s = StrUtil.blankToDefault(scanner.nextLine(), "Y");
            answer.openAuth(s);
        } while (!yesOrNo.contains(s));
        do {
            System.out.print("输入要生成的表（支持%模糊，所有输入 %）：");
            s = scanner.nextLine();
            answer.targetTable(s);
        } while (StrUtil.isBlank(s));
        return answer;
    }

    @Getter
    public static class Answer {

        private final String PROJECT_PATH = System.getProperty("user.dir") + "/qk-money-app";

        /**
         * 全局配置
         */
        private final GlobalConfig.Builder globalConfig = new GlobalConfig.Builder();

        /**
         * 包配置
         */
        private final PackageConfig.Builder packageConfig = new PackageConfig.Builder();

        /**
         * 注入配置
         */
        private final InjectionConfig.Builder injectionConfig = new InjectionConfig.Builder();

        /**
         * 策略配置
         */
        private final StrategyConfig.Builder strategyConfig = new StrategyConfig.Builder();

        /**
         * 自定义参数
         */
        private final Map<String, Object> customMap = new HashMap<>();

        /**
         * 自定义文件
         */
        private final List<CustomFile.Builder> customFileBuilders = this.loadCustomFileBuilders();


        public Answer() {
            globalConfig
                    .disableOpenDir()
                    .outputDir(PROJECT_PATH + "/money-app-biz/src/main/java/");
            packageConfig
                    .parent("com.money")
                    .pathInfo(MapUtil.builder(new HashMap<OutputFile, String>())
                            .put(OutputFile.entity, PROJECT_PATH + "/money-app-api/src/main/java/com/money/entity")
                            .put(OutputFile.xml, PROJECT_PATH + "/money-app-biz/src/main/resources/mapper")
                            .build());
            customMap.put("packageOther", "com.money.dto");
            injectionConfig
                    .beforeOutputFile((tableInfo, objectMap) -> {
                        // 可以在这里添加自定义逻辑，如修改 objectMap 中的配置
                    })
                    .customMap(customMap);
            strategyConfig
                    .entityBuilder()
                    .enableLombok()
                    .enableChainModel()
                    .disableSerialVersionUID()
                    .idType(IdType.ASSIGN_ID);
            strategyConfig.mapperBuilder()
                    .enableBaseColumnList()
                    .enableBaseResultMap();
            strategyConfig.serviceBuilder()
                    .formatServiceFileName("%sService");
            strategyConfig.controllerBuilder()
                    .enableRestStyle();
        }

        private List<CustomFile.Builder> loadCustomFileBuilders() {
            String dtoFilePath = PROJECT_PATH + "/money-app-api/src/main/java/com/money/dto";
            CustomFile.Builder queryDTO = new CustomFile.Builder().fileName("").filePath(dtoFilePath)
                    .formatNameFunction((tableInfo -> tableInfo.getEntityName().toLowerCase() + File.separator + tableInfo.getEntityName() + "QueryDTO.java"))
                    .templatePath("/templates/queryDto.java.ftl");
            CustomFile.Builder DTO = new CustomFile.Builder().fileName("").filePath(dtoFilePath)
                    .formatNameFunction((tableInfo -> tableInfo.getEntityName().toLowerCase() + File.separator + tableInfo.getEntityName() + "DTO.java"))
                    .templatePath("/templates/dto.java.ftl");
            CustomFile.Builder VO = new CustomFile.Builder().fileName("").filePath(dtoFilePath)
                    .formatNameFunction((tableInfo -> tableInfo.getEntityName().toLowerCase() + File.separator + tableInfo.getEntityName() + "VO.java"))
                    .templatePath("/templates/vo.java.ftl");
            return ListUtil.of(queryDTO, DTO, VO);
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
                globalConfig.enableSpringdoc();
            }
        }

        /**
         * 文件覆盖
         *
         * @param yesOrNo 是或否
         */
        public void fileOverride(String yesOrNo) {
            if ("Y".equalsIgnoreCase(yesOrNo)) {
                strategyConfig.entityBuilder().enableFileOverride();
                strategyConfig.mapperBuilder().enableFileOverride();
                strategyConfig.serviceBuilder().enableFileOverride();
                strategyConfig.controllerBuilder().enableFileOverride();
                customFileBuilders.forEach(CustomFile.Builder::enableFileOverride);
            }
            injectionConfig.customFile(customFileBuilders.stream().map(CustomFile.Builder::build).collect(Collectors.toList()));
        }

        /**
         * 生成xml
         *
         * @param yesOrNo 是或否
         */
        public void generateXml(String yesOrNo) {
            if ("N".equalsIgnoreCase(yesOrNo)) {
                strategyConfig.mapperBuilder().disableMapperXml();
            }
        }

        /**
         * 开放身份验证
         *
         * @param yesOrNo 是或否
         */
        public void openAuth(String yesOrNo) {
            customMap.put("preAuthorize", "Y".equalsIgnoreCase(yesOrNo));
        }

        /**
         * 目标表
         *
         * @param table 表格
         */
        public void targetTable(String table) {
            if ("%".equals(table)) {
                strategyConfig.addInclude(".*");
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
            if ("Y".equalsIgnoreCase(yesOrNo)) {
                strategyConfig.entityBuilder()
                        .superClass(BaseEntity.class)
                        .addSuperEntityColumns("create_time", "create_by", "update_by", "update_time", "id");
            }
        }
    }
}
