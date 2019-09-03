package com.fangyuan.service;

import com.fangyuan.domain.User;
import com.fangyuan.mapper.UserMapper;
import com.fangyuan.vo.PageResult;
import com.fangyuan.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //分页查询
    public PageResult<User> findUserByPage(Integer page, Integer limit, String realname) {
        //设置分页
        PageHelper.startPage(page, limit);
        Example example = new Example(User.class);
        List<User> userList = null;

        if (realname == null || realname == "") {
            userList = userMapper.selectByExample(example);
        } else {
            example.createCriteria().orLike("realname", "%" + realname + "%").orLike("username", "%" + realname + "%");

            userList = userMapper.selectByExample(example);
        }

        PageInfo<User> pageInfo = new PageInfo<>(userList);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());

    }

    //通过id找用户名
    public String findUserNameByUserId(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return user.getUsername();
    }

    //新增
    public void add(User user) {

        //System.out.println(user.toString());

        userMapper.insert(user);
    }

    //登录
    public Result login(User user) {

        //修改：获取传递过来的
        Result result = new Result();
        //根据前端传递过来的用户名密码查询到的数据库中的User

        String username = user.getUsername();
        String password = user.getPassword();
        User newUser = new User();
        newUser.setUsername(username);
        List<User> userList = userMapper.select(newUser);
        if (userList != null) {
            //首先用户名存在！
            //接着判断密码
            User user1 = userList.get(0);
            String password1 = user1.getPassword();
            if (password.equals(password1)) {

                result.setFlag(true);
                result.setMessage("登录成功！");
            } else {
                result.setFlag(false);
                result.setMessage("密码输入有误，请确认后登录！");
            }
        } else {
            result.setFlag(false);
            result.setMessage("用户名不存在！");
        }
        return result;
    }

    //修改个人信息
    public Result update(User user) {
        System.out.println(user);
        try {
            userMapper.updateByPrimaryKey(user);
            return new Result(true, "更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败！");
        }
    }

    //查询个人信息
    public User query(String username) {
        User user = new User();
        user.setUsername(username);
        List<User> list = userMapper.select(user);
        User user1 = list.get(0);
        //System.out.println(user1);
        return user1;
    }

    //修改密码
    public Result updatePwd(String username, String oldPwd, String newPwd) {
        try {

            User user = new User();
            user.setUsername(username);
            user.setPassword(oldPwd);

            List<User> list = userMapper.select(user);
            User user1 = list.get(0);
            user1.setPassword(newPwd);

            userMapper.updateByPrimaryKey(user1);

            return new Result(true, "密码修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "原密码输入错误！");
        }

    }

    public Result check(String username) {
        User user = new User();
        user.setUsername(username);
        List<User> list = userMapper.select(user);

        //如果能查询到数据，说明改用户名存在
        if (list != null && list.size() > 0) {
            return new Result(true, "用户名已存在！");
        } else {
            return new Result(false, "用户名可使用！");
        }
    }
}
