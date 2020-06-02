package com.qjp.sec_kill.controller;

import com.qjp.sec_kill.domain.User;
import com.qjp.sec_kill.rabbitmq.MQsenders;
import com.qjp.sec_kill.redis.RedisService;
import com.qjp.sec_kill.redis.UserKey;
import com.qjp.sec_kill.result.CodeMsg;
import com.qjp.sec_kill.result.Result;
import com.qjp.sec_kill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQsenders mQsenders;


    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> home() {
        return Result.success("Hello，world");
    }

    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SESSION_ERROR);
    }

    @RequestMapping("/hello/themaleaf")
    public String themaleaf(Model model) {
        model.addAttribute("name", "qjp");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }
    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq() {
        mQsenders.send("hello QJP");
        return Result.success("hello world");
    }

    @RequestMapping("/db/get/{name}")
    @ResponseBody
    public Result<User> dbGetByName(@PathVariable("name") String name) {
        User user = userService.getByName(name);
        return Result.success(user);
    }

    @RequestMapping("/db/insert/{name}")
    @ResponseBody
    public Result<Integer> dbInsert(@PathVariable("name") String name) {
        User user= new User(name);
        Integer insert = userService.insertUser(user);
        return Result.success(insert);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User  user  = redisService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user  = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(true);
    }



}