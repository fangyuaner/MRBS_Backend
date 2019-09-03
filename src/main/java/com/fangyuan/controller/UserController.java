package com.fangyuan.controller;

import com.fangyuan.domain.User;
import com.fangyuan.service.UserService;
import com.fangyuan.vo.PageResult;
import com.fangyuan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/users")

public class UserController {

    @Autowired
    private UserService userService;

    //分页查询
    @GetMapping
    public ResponseEntity<PageResult<User>> findUserByPage(
            @RequestParam(name = "page")Integer page,
            @RequestParam(name = "limit")Integer limit,
            @RequestParam(name = "realname")String realname
    ){
        PageResult<User> userList = userService.findUserByPage(page,limit,realname);
        return ResponseEntity.ok(userList);
    }

    //登录
    @ResponseBody
    @RequestMapping("login")
    public Result login(@RequestBody User user){
        return userService.login(user);
    }

    //修改个人信息
    @ResponseBody
    @RequestMapping("profile")
    public Result update(@RequestBody User user){
        return userService.update(user);
    }

    //回显个人信息
    @ResponseBody
    @GetMapping("query")
    public User query(@RequestParam(name = "username")String username){
        //System.out.println(username);
        User user = userService.query(username);
        return user;
    }

    //修改密码
    @ResponseBody
    @PutMapping("changePwd")
    public Result updatePwd(
            @RequestParam(name = "oldPwd" /*,required = false*/)String oldPwd,
            @RequestParam(name = "newPwd" /*,required = false*/)String newPwd,
            @RequestParam(name = "username" /*, required = false*/)String username
    ){
        Result result = userService.updatePwd(username, oldPwd, newPwd);
        return result;
    }

}
