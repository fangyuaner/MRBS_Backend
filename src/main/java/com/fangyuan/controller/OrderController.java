package com.fangyuan.controller;

import com.fangyuan.domain.Order;
import com.fangyuan.domain.Room;
import com.fangyuan.domain.User;
import com.fangyuan.service.OrderService;
import com.fangyuan.service.RoomService;
import com.fangyuan.vo.PageResult;
import com.fangyuan.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("api/v1/order")

public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private RoomService roomService;

    //分页查询
    @GetMapping
    public ResponseEntity<PageResult<Order>> findOrderByPage(
            @RequestParam(name = "page")Integer page,
            @RequestParam(name = "limit")Integer limit,
            @RequestParam(name = "meettingname")String meettingname
    ){
        //Long roomid = roomService.findRoomIdByRoomName(roomname);
        PageResult<Order> list =  orderService.findOrderByPage(page,limit,meettingname);
        return ResponseEntity.ok(list);
    }

    //查询roomname
    @ResponseBody
    @GetMapping("findRoomName")
    public Result findRoomName(String roomname){
        System.out.println(roomname);
          return roomService.findAllRoomName(roomname);
    }

    //进行预约
    @ResponseBody
    @PostMapping
    public Result add(@RequestBody Order order){

        System.out.println(order);
        Date createTime = order.getCreateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(createTime);
        System.out.println(time);

        return orderService.add(order);
    }

    //修改信息
    @ResponseBody
    @PutMapping
    public Result update(@RequestBody Order order){

        return orderService.update(order);
    }

    //删除数据
    @ResponseBody
    @DeleteMapping("delete")
    public Result remove(
            @RequestParam("id") Long id,
            @RequestParam("username")String username
    ){
        return orderService.remove(id,username);
    }
    //批量删除数据    管理员
    @ResponseBody
    @DeleteMapping("batch")
    public Result removeBatch(
            @RequestParam("id") String ids,
            @RequestParam("username")String username
    ){
        return orderService.removeBatch(ids,username);
    }
}
