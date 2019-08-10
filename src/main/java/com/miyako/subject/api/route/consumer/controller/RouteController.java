package com.miyako.subject.api.route.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.miyako.subject.dubbo.aop.MethodLog;
import com.miyako.subject.service.course.route.RouteCourseService;
import com.miyako.subject.service.user.api.RouteUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
    @MethodLog(value = "RouteController", operationType = "服务器转发", operationName = "user",operationArgs = "用户列表")
    public String user(HttpServletRequest request, String path){
        routeUserService.info();
        return redirect(request,RpcContext.getContext().getRemoteHost(), userServicePort, path);
    }

    @GetMapping("/course")
    @MethodLog(value = "RouteController", operationType = "服务器转发", operationName = "course",operationArgs = "课程列表")
    public String course(HttpServletRequest request, String path){
        routeCourseService.info();
        return redirect(request,RpcContext.getContext().getRemoteHost(), courseServicePort, path);
    }

    private String redirect(String ip, String port, String path){
        logger.info("pc->" + ip + ":" +port);
        String url = String.format("redirect:http://%s:%s%s",ip,port,path);
        logger.info("url===>"+url);
        return url;
    }

    private String redirect(HttpServletRequest request, String ip, String port, String path){
        logger.info("pc->" + ip + ":" +port);
        String url ;
        if(request == null ){
            url = String.format("redirect:http://%s:%s%s",ip,port,path);
        }else {
            String token = getCookieValue(request,"token");
            url = String.format("redirect:http://%s:%s%s?token=%s",ip,port,path, token);
        }
        logger.info("url===>"+url);
        return url;
    }

    @MethodLog(value = "RouteController", operationType = "拦截器", operationName = "getCookieValue", operationArgs = "获取cookie")
    public String getCookieValue(HttpServletRequest request, String cookie1NameToken) {//COOKIE1_NAME_TOKEN-->"token"
        //遍历request里面所有的cookie
        Cookie[] cookies=request.getCookies();
        if(cookies!=null) {
            for(Cookie cookie :cookies) {
                if(cookie.getName().equals(cookie1NameToken)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
