package com.jinmy.onlinejudge.config;

import com.jinmy.onlinejudge.interceptor.SessionHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> stringList=new ArrayList<>();
        stringList.add("/admin/**");
        registry.addInterceptor(new SessionHandlerInterceptor()).addPathPatterns(stringList);
    }
}
