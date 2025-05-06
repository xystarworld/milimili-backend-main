package com.nebulaxy.milimilibackendmain.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 添加自定义拦截器，设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                // 拦截规则
                .addPathPatterns("/comments/**","/content/upload","/content/filesUpload","/content/chunkUpload"
                        ,"/content/filesAdd","/content/posting","/likePost/**","/content/favorites"
                        ,"/content/selectFavorites","/content/unlikePost/**"
                )
                // 排除接口
                .excludePathPatterns("/comments/selectComment","/comments/chunkUpload","/comments/checkChunk");
    }

    @Bean
    public JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
    }
}
