package com.slz.xblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author : SunLZ
 * @project : Blog
 * @date : 2024/10/21
 */
@SpringBootApplication
@EnableOpenApi
@EnableAspectJAutoProxy // 开启AOP
@MapperScan(basePackages = "com.slz.xblog.mapper")
@EnableCaching // 开启缓存，（使用注解缓存）
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
