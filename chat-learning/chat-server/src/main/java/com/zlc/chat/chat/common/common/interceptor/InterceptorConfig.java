package com.zlc.chat.chat.common.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--15:01
 * 3. 目的:
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor interceptor;
    @Autowired
    private CollectInterceptor collectInterceptor;

    @Autowired
    private BlackInterceptor blackInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/capi/**");

        registry.addInterceptor(collectInterceptor)
                .addPathPatterns("/capi/**");

        registry.addInterceptor(blackInterceptor)
                .addPathPatterns("/capi/**");
    }
}
