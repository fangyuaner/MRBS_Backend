package com.fangyuan.controller;

import com.fangyuan.domain.Room;
import com.fangyuan.domain.User;
import com.fangyuan.service.RoomService;
import com.fangyuan.service.UserService;
import com.fangyuan.vo.PageResult;
import com.fangyuan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/books")

public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    //获取全部
    @GetMapping
    public ResponseEntity<PageResult<Room>> findRoomByPage(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam("roomname") String roomname
    ) {
        PageResult<Room> roomList = roomService.findRoomByPage(page, limit, roomname);
        return ResponseEntity.ok(roomList);
    }

    //修改信息  管理员
    @ResponseBody
    @PutMapping
    public Result update(@RequestBody Room room) {
        return roomService.update(room);
    }

    //删除数据  管理员
    @ResponseBody
    @DeleteMapping("delete")
    public Result remove(
            @RequestParam("id") Long id,
            @RequestParam("username")String username
    ) {
        return  roomService.remove(id,username);
    }

    //批量删除数据    管理员
    @ResponseBody
    @DeleteMapping("batch")
    public Result removeBatch(
            @RequestParam("ids") String ids,
            @RequestParam("username") String username
    ) {

        return roomService.removeBatch(ids,username);
    }

    //数据新增  管理员
    @ResponseBody
    @PostMapping
    public Result add(@RequestBody Room room) {
        return roomService.add(room);
    }


}
