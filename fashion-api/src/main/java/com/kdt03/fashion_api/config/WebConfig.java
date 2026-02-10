package com.kdt03.fashion_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/uploads/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/clothimage/");

        // Swagger 수동 매핑 제거 (라이브러리 자동 설정 사용)
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/product-list").setViewName("productList");
        registry.addViewController("/test-page").setViewName("uploadTest");
        registry.addViewController("/profile_test").setViewName("profile_test");

        // Swagger 리다이렉트 수동 설정 제거
    }

}