package com.slz.xblog.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author : SunLZ
 * @project : Blog
 * @date : 2024/10/21
 */
// @Configuration : 配置类，相当于 applicationContext.xml文件中 <beans> 标签
@Configuration
public class Swagger3Config {
    @Bean // Bean 注解，将返回值放入 Spring 容器，相当于 applicationContext.xml文件中 <bean> 标签
    public Docket apiConfig() { // 方法名可以任意，返回值类型必须是 Docket
        // Swagger3 使用 OAS_30， Swagger2 使用 SWAGGER_2
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                // 设置通过什么方式定位到需要生成文档的接口
                // 定位了方法上的 @ApiOperation 注解
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()) // 设置哪些接口暴露给Swagger展示，any() 表示所有, regex() 表示正则匹配
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("XBlog")
                .description("XBlog 接口文档")
                .contact(new Contact("sunlinzhao", "https://github.com/sunlinzhao/XBlog", "2436949535@qq.com"))
                .version("1.0")
                .build();
    }
}