package com.miyako.subject.api.route.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.miyako.subject.commons.domain.TbStudent;
import com.miyako.subject.dubbo.aop.MethodLog;
import com.miyako.subject.service.redis.api.RedisService;
import com.miyako.subject.service.redis.key.StudentKey;
import com.miyako.subject.service.user.api.TbUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * ClassName LoginController
 * Description //TODO
 * Author weila
 * Date 2019-08-09-0009 09:48
 */
@Controller
public class LoginController{

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Reference
    private TbUserService studentSetvice;

    @Reference
    private RedisService redisService;

    @GetMapping("/login")
    @MethodLog(value = "LoginController", operationType = "路径访问", operationName = "login")
    public String login(){
        logger.info("get /login");
        return "login";
    }

    @PostMapping("/login")
    @MethodLog(value = "LoginController", operationType = "路径访问", operationName = "login",operationArgs = "用户名和密码")
    public String login(String studentnum, String password, HttpServletResponse response){
        logger.info(studentnum+"->"+password);
        TbStudent student = new TbStudent();
        student.setName(studentnum);
        student.setPassword(password);
        TbStudent one = studentSetvice.selectOne(student);
        if(one == null){
            logger.info("student no exist...");
            return "redirect:login";
        }else{
            logger.info("student set cache...");
            // 生成登陆token
            String token = UUID.randomUUID().toString().replace("-", "");//去掉原生的"-"
            //将token写入redis缓存
            addCookie(one,token,response);
            return "redirect:/route/course?path=/course/list&token="+token;
       }
    }

    @MethodLog(value = "LoginController", operationType = "添加cookie", operationName = "addCookie",operationArgs = "用户和token")
    public void addCookie(TbStudent user, String token, HttpServletResponse response){
        // 设置登陆token进缓存
        redisService.set(StudentKey.token,token,user);
        redisService.set(StudentKey.getById,user.getId().toString(),user);
        Cookie cookie = new Cookie("token", token);
        // 设置cookie的有效期，与session有效期一致
        cookie.setMaxAge(StudentKey.token.expireSeconds());
        // 设置网站的根目录
        cookie.setPath("/");
        // 需要写到response中
        response.addCookie(cookie);
    }

    @GetMapping("/admin")
    public String admin(){
        logger.info("get /admin");
        return "admin";
    }


    @PostMapping("/admin")
    @MethodLog(value = "LoginController", operationType = "路径访问", operationName = "admin",operationArgs = "用户名和密码")
    public String admin(String adminnum, String password){
        logger.info(adminnum+"->"+password);
        //TbStudent student = new TbStudent();
        //student.setName(studentnum);
        //student.setPassword(password);
        //TbStudent one = studentSetvice.selectOne(student);
        //if(one == null){
        //    logger.info("student no exist...");
        //}else{
        //    logger.info("student set cache...");
        //    redisService.set(StudentKey.getById,one.getId().toString(),TbStudent.class);
        //}
        return "redirect:/route/user?path=/user/list";
    }
}
