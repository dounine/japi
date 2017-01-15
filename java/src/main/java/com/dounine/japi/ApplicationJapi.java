package com.dounine.japi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by huanghuanlai on 2017/1/15.
 */
@SpringBootApplication
@ComponentScan(basePackages={"com.dounine.japi"})
public class ApplicationJapi extends WebMvcConfigurerAdapter{

    public static void main(String[] args) {
        SpringApplication.run(ApplicationJapi.class,args);
    }



}
