package com.fangyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/user/listpage")
    public String hello(){
        return "hello world! 启动了！";
    }
}
