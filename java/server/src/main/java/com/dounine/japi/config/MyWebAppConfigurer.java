package com.dounine.japi.config;

import com.dounine.japi.config.ErrorRequestInterceptor;
import com.dounine.japi.config.LoginRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by huanghuanlai on 2017/2/27.
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ErrorRequestInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new LoginRequestInterceptor()).addPathPatterns("/**").excludePathPatterns(new String[]{"/user/login","/user/isLogin","/user/onlines"});
        super.addInterceptors(registry);
    }
}
