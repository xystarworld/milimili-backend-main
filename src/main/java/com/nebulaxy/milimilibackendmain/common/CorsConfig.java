package com.nebulaxy.milimilibackendmain.common;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();


        config.setAllowedOriginPatterns(List.of("*"));

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // 是否允许发送cookie等凭证
        config.setAllowCredentials(true);

        // 设置预检请求的缓存时间（单位：秒）
        config.setMaxAge(3600L);

        // 暴露特定的响应头
        config.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
