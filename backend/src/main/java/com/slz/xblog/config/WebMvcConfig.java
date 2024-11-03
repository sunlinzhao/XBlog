package com.slz.xblog.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/22
 */
public class WebMvcConfig implements WebMvcConfigurer {

    // 配置跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://139.155.158.230:17011") // 允许的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 允许携带凭证
    }
}
