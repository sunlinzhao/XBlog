package com.slz.xblog.config;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/28
 */
import com.slz.xblog.listener.CustomSessionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionListener;

@Configuration
public class SessionConfig {
    @Bean
    public HttpSessionListener httpSessionListener() {
        return new CustomSessionListener();
    }
}
