package com.dounine.japi;

import com.dounine.japi.handler.MappingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ike on 17-1-16.
 */
@Component
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private MappingHandler mappingHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mappingHandler);
    }
}
