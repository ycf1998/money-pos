package com.money.mb;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.money.mb.base.BaseEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : mybatis plus 代码生成器
 * @createTime : 2022-01-01 16:36:00
 */
public class MybatisPlusGenerator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://127.0.0.1:3306/qk_money?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2b8&useSSL=false", "root", "root");

    public static void main(String[] args) {
        String javaDir = new File("qk-money-app/money-app-biz/src/main/java/").getAbsolutePath();

        Map<OutputFile, String> pathInfo = new HashMap<>();
        pathInfo.put(OutputFile.entity, new File("qk-money-app/money-app-api/src/main/java/com/money/entity").getAbsolutePath());
        pathInfo.put(OutputFile.mapperXml, new File("qk-money-app/money-app-biz/src/main/resources/mapper").getAbsolutePath());
        pathInfo.put(OutputFile.other, new File("qk-money-app/money-app-api/src/main/java/com/money/web").getAbsolutePath());

        Map<String, String> DTOAndVOTemplateMap = new HashMap<>();
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig((scanner, builder) -> builder.disableOpenDir()
                        .author(scanner.apply("请输入作者名称？"))
                        // 开启swagger
                        .enableSwagger()
                        // 是否覆盖
                        .fileOverride()
                        .outputDir(javaDir))
                .templateConfig(builder -> builder.disable(TemplateType.XML))
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent("com.money").other("web").pathInfo(pathInfo))
                // 注入配置
                .injectionConfig(builder -> builder
                        .beforeOutputFile((tableInfo, objectMap) -> {
                            DTOAndVOTemplateMap.clear();
                            DTOAndVOTemplateMap.put(tableInfo.getEntityName() + "QueryDTO.java", "/templates/queryDto.java.ftl");
                            DTOAndVOTemplateMap.put(tableInfo.getEntityName() + "DTO.java", "/templates/dto.java.ftl");
                            DTOAndVOTemplateMap.put(tableInfo.getEntityName() + "VO.java", "/templates/vo.java.ftl");
                        })
                        .customFile(DTOAndVOTemplateMap))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder
                        // 不生成sys相关表
                        .likeTable(new LikeTable("demo"))
//                        .notLikeTable(new LikeTable("sys"))
                        // Entity策略
                        .entityBuilder().superClass(BaseEntity.class).enableLombok()
                        .addIgnoreColumns("create_time", "create_by", "update_by", "update_time", "id")
                        // Mapper策略
                        .mapperBuilder().enableBaseColumnList().enableBaseResultMap()
                        // Service策略
                        .serviceBuilder().formatServiceFileName("%sService")
                        // Controller策略
                        .controllerBuilder().enableRestStyle()
                        .build())
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
