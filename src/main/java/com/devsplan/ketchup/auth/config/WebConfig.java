//package com.devsplan.ketchup.auth.config;
//
//
//
//import com.devsplan.ketchup.auth.filter.HeaderFilter;
//import com.devsplan.ketchup.auth.interceptor.JwtTokenInterceptor;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///*
//* Web configuration을 위한 클래스
//* */
//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//
//    //요청이 들어왔을때 정적자원은 허용해주기 위함
//    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
//            "classpath:/static/", "classpath:/public/", "classpath:/", "classpath:/resources/","classpath:/profile/",
//            "classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/"
//    };
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
//    }
//
//    @Bean
//    public FilterRegistrationBean<HeaderFilter> getFilterRegistrationBean() {
//        FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<HeaderFilter>(createHeaderFilter());
//        registrationBean.setOrder(Integer.MIN_VALUE); //필터의 순서를 가장 처음으로 올려줌
//        registrationBean.addUrlPatterns("/*"); //모든 요청들이 필터를 타게끔 해준다.
//        return registrationBean;
//    }
//
//    @Bean
//    public HeaderFilter createHeaderFilter() {
//        return new HeaderFilter();
//    }
//
//    @Bean
//    public JwtTokenInterceptor jwtTokenInterceptor() {
//        return new JwtTokenInterceptor();
//    }
//}
