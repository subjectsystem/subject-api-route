package com.miyako.subject.api.route.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ImportResource;

/**
 * ClassName SubjectApiRouteConsumerApplication
 * Description //TODO
 * Author weila
 * Date 2019-08-08-0008 13:50
 */
@EnableHystrix
@EnableHystrixDashboard
@ImportResource(value = {"classpath:route-consumer.xml"})
@SpringBootApplication(scanBasePackages = "com.miyako.subject", exclude = DataSourceAutoConfiguration.class)
public class SubjectApiRouteConsumerApplication{

    private static Logger logger = LoggerFactory.getLogger(SubjectApiRouteConsumerApplication.class);

    public static void main(String[] args){
        SpringApplication.run(SubjectApiRouteConsumerApplication.class, args);
        logger.info("===>:SubjectApiRouteConsumerApplication start...");
    }
}
