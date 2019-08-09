package com.miyako.subject.api.route.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.miyako.subject.commons.domain.TbStudent;
import com.miyako.subject.service.redis.api.RedisService;
import com.miyako.subject.service.redis.key.StudentKey;
import com.miyako.subject.service.user.api.TbUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String login(){
        logger.info("get /login");
        return "login";
    }

    @PostMapping("/login")
    public String login(String studentnum, String password){
        logger.info(studentnum+"->"+password);
        TbStudent student = new TbStudent();
        student.setName(studentnum);
        student.setPassword(password);
        TbStudent one = studentSetvice.selectOne(student);
        if(one == null){
            logger.info("student no exist...");
        }else{
            logger.info("student set cache...");
            redisService.set(StudentKey.token,one.getId().toString(),TbStudent.class);
        }
        return "redirect:/route/course?path=/course/list/1";
    }

    @GetMapping("/admin")
    public String admin(){
        logger.info("get /admin");
        return "admin";
    }


    @PostMapping("/admin")
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
        return "redirect:/route/user?path=/user/list/1";
    }
}
