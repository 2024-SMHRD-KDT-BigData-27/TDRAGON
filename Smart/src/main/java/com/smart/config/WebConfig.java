//package com.smart.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // /audio/** 요청이 C:/uploads/ 폴더로 매핑되도록 설정
//        registry.addResourceHandler("/audio/**")
//                .addResourceLocations("file:///C:/uploads/");
//    }
//}