package com.mdrsolutions.records_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
public class HtmxThymeleafViewResolverConfigurer {

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Bean(name = "viewResolver")
    public ThymeleafViewResolver viewResolver(){
        return thymeleafViewResolver;
    }
}
