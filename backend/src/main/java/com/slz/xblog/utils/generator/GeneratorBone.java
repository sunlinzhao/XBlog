package com.slz.xblog.utils.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author : SunLZ
 * @project : Blog
 * @date : 2024/10/21
 */
@Component
public class GeneratorBone {
    @Value("${db.url}")
    private String URL;
    @Value("${db.username}")
    private String USERNAME;
    @Value("${db.password}")
    private String PASSWORD;
    @Value("${generator.global.outputDir}")
    private String OUTPUT_DIR; // 生成代码输出目录
    @Value("${generator.global.author}")
    private String AUTHOR;
    @Value("${generator.package.packageName}")
    private String PACKAGE_NAME;
    @Value("${generator.package.modelName}")
    private String MODEL_NAME;
    @Value("${generator.package.xmlPath}")
    private String XML_PATH;
    @Value("${generator.entity.tableName}")
    private String TABLE_NAME;
    @Value("${generator.entity.superClass}")
    private String SUPER_CLASS;
    @Value("${generator.entity.suffix}")
    private String SUFFIX;


    public void run() {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD) // 数据库连接
                .templateEngine(new FreemarkerTemplateEngine()) // 模板引擎 Freemarker
                .globalConfig(builder -> { // 全局配置
                    builder.author(AUTHOR) // 设置作者
                            .outputDir(OUTPUT_DIR) // 指定输出目录
                            .enableSwagger() // 开启 swagger 注解
                    ;
                })
                .packageConfig(builder -> { // 包配置
                    builder.parent(PACKAGE_NAME) // 设置父包名
                            .moduleName(MODEL_NAME) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, XML_PATH)) // 设置mapperXml生成路径
                    ;
                })
                .strategyConfig(builder -> { // 策略配置
                    builder.addInclude(TABLE_NAME) // 设置需要生成的表名

                            .entityBuilder() // 实体类配置
                            .formatFileName("%s" + SUFFIX)// 格式化文件名称
                            .superClass(SUPER_CLASS) // 设置父类
                            .enableChainModel() // 链式调用
                            .enableLombok() // lombok注解
                            .enableTableFieldAnnotation()
                            .addIgnoreColumns("createTime", "updateTime", "createBy", "updateBy", "remark", "version")

                            .mapperBuilder()  // mapper 配置
                            .enableMapperAnnotation()

                            .serviceBuilder() // service 配置

                            .controllerBuilder() // controller 配置
                            .enableRestStyle()
                    ;
                }).execute();
    }
}
