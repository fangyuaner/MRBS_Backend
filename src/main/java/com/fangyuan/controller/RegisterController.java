package com.fangyuan.controller;

import com.fangyuan.domain.User;
import com.fangyuan.service.UserService;
import com.fangyuan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    //注册
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody User user){
        System.out.println(user.getPassword());
        userService.add(user);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //用户名检查
    @ResponseBody
    @PutMapping("check")
    public Result check(@RequestParam(name = "username" )String username){
        Result result = userService.check(username);
        return result;
    }
}
