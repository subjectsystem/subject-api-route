package com.miyako.subject.api.route.consumer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName DefaultView
 * Description //TODO
 * Author weila
 * Date 2019-08-09-0009 10:08
 */
@Configuration
public class DefaultView implements WebMvcConfigurer{

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("welcome");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
