package com.miyako.subject.api.route.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.miyako.subject.service.course.route.RouteCourseService;
import com.miyako.subject.service.user.api.RouteUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName RouteController
 * Description //TODO
 * Author weila
 * Date 2019-08-08-0008 14:16
 */
@Controller
@RequestMapping("/route")
public class RouteController{

    private static Logger logger = LoggerFactory.getLogger(RouteController.class);

    @Reference
    private RouteUserService routeUserService;
    @Value(value = "${services.ports.user}")
    private String userServicePort;

    @Reference
    private RouteCourseService routeCourseService;
    @Value(value = "${services.ports.course}")
    private String courseServicePort;

    @GetMapping("/user")
    public String user(String path){
        routeUserService.info();
        return redirect(RpcContext.getContext().getRemoteHost(), userServicePort, path);
    }

    @GetMapping("/course")
    public String course(String path){
        routeCourseService.info();
        return redirect(RpcContext.getContext().getRemoteHost(), courseServicePort, path);
    }

    private String redirect(String ip, String port, String path){
        logger.info("pc->" + ip + ":" +port);
        return String.format("redirect:http://%s:%s%s",ip,port,path);
    }
}
