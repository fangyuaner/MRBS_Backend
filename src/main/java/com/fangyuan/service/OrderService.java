package com.fangyuan.service;

import com.fangyuan.domain.Order;
import com.fangyuan.domain.Room;
import com.fangyuan.domain.User;
import com.fangyuan.mapper.OrderMapper;
import com.fangyuan.vo.PageResult;
import com.fangyuan.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    public PageResult<Order> findOrderByPage(Integer page, Integer limit, String meettingname) {
        //设置分页
        PageHelper.startPage(page, limit);
        Example example = new Example(Order.class);
        List<Order> orderList = null;

        if (meettingname == null || meettingname == "") {
            orderList = orderMapper.selectByExample(example);

        } else {
            example.createCriteria().orLike("meettingname", "%" + meettingname + "%");

            orderList = orderMapper.selectByExample(example);
        }

        //预想是，在进行展示时，获取order的createTime和endTime，去与当前时间毫秒值比较，以此重新设定statu，0（预约成功），1（会议进行中），2（会议结束）
        Date date = new Date();     //获取当前时间
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis(); //当前时间毫秒值


        /*此处查询道德orderList中的数据中包含roomid和userid，但我们需要的是roomname和username
         * 先取出来去查寻*/
        for (Order order : orderList) {
            Long roomId = order.getRoomId();
            String roomName = roomService.findRoomNameByRoomId(roomId);
            Long userId = order.getUserId();
            String userName = userService.findUserNameByUserId(userId);
            order.setRoomname(roomName);
            order.setUsername(userName);

            Date createTime = order.getCreateTime();    //获取预约时间
            long createTimeTime = createTime.getTime();//预约时间毫秒值
            Date endTime = order.getEndTime();
            long endTimeTime = endTime.getTime();//结束时间毫秒值

            if (timeInMillis < createTimeTime) {
                order.setStatu("0");//会议未开始
                orderMapper.updateByPrimaryKey(order);
            } else if (timeInMillis >= createTimeTime && timeInMillis <= endTimeTime) {
                order.setStatu("1");//会议进行中
                orderMapper.updateByPrimaryKey(order);
            } else {
                order.setStatu("2");//会议结束
                orderMapper.updateByPrimaryKey(order);
            }
        }
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }


    //order申请
    public Result add(Order order) {
        Result result = new Result();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis(); //当前时间毫秒值
        long createTimeTime = order.getCreateTime().getTime();//预约时间毫秒值
        long endTimeTime = order.getEndTime().getTime();

        //通过username找到userId，将其设置到order中
        String username = order.getUsername();
        Long id = userService.query(username).getId();
        order.setUserId(id);

        //获取当前order中人数
        String capacity = order.getCapacity();
        int capat = Integer.parseInt(capacity);

        //获取当前目标会议室的容量
        Long roomId = order.getRoomId();
        int roomCap = roomService.findRoomCapById(roomId);

        if (createTimeTime >= (timeInMillis + 1800000) && endTimeTime > createTimeTime) {
            //时间判断可以，进行预约
            if (capat <= roomCap / 3) {
                result.setMessage("人数较少，建议更换目标会议室！");
                result.setEncode(0);
                order.setStatu("0");
                orderMapper.insert(order);
            } else if (capat > roomCap) {
                result.setMessage("人数超过目标会议室容量，请重新预约！");
                result.setEncode(1);
            } else {
                result.setMessage("预约成功");
                result.setEncode(2);
                order.setStatu("0");
                orderMapper.insert(order);
            }
            return result;
        } else {
            result.setEncode(4);
            result.setMessage("预约异常,请提前30分钟进行预约！顺便确认会议的开始结束时间！");
            return result;
        }

    }


    //数据更新
    public Result update(Order order) {
        Result result = new Result();

        //获取当前order中人数
        String capacity = order.getCapacity();
        int capat = Integer.parseInt(capacity);

        //获取当前目标会议室的容量
        Long roomId = order.getRoomId();
        int roomCap = roomService.findRoomCapById(roomId);

        if (capat <= roomCap / 3) {
            result.setMessage("人数较少，建议更换目标会议室，重新预约！");
            result.setEncode(0);
            orderMapper.updateByPrimaryKey(order);
        } else if (capat > roomCap) {
            result.setMessage("人数超过目标会议室容量，请重新预约");
            result.setEncode(1);
        } else {
            result.setMessage("修改成功");
            result.setEncode(2);
            orderMapper.updateByPrimaryKey(order);
        }

        return result;
    }

    //单个删除
    public Result remove(Long id, String username) {
        Result result = new Result();
        Order order1 = orderMapper.selectByPrimaryKey(id);
        String username1 = order1.getUsername();
        if (username.equals(username1)) {
            Order order = orderMapper.selectByPrimaryKey(id);
            Date createTime = order.getCreateTime();
            long createTimeTime = createTime.getTime();
            Date endTime = order.getEndTime();
            long endTimeTime = endTime.getTime();
            Date date = new Date();     //获取当前时间
            Calendar calendar = Calendar.getInstance();
            long timeInMillis = calendar.getTimeInMillis(); //当前时间毫秒值

            if (timeInMillis < createTimeTime || timeInMillis > endTimeTime) {
                result.setEncode(0);
                result.setMessage("删除成功");
                orderMapper.deleteByPrimaryKey(id);
            } else {
                result.setEncode(1);
                result.setMessage("会议正在进行，不可删除");
            }
        } else {
            result.setEncode(2);
            result.setMessage("对不起，您不可以取消别人的预约请求！");
        }

        return result;
    }

    //批量删除  管理员操作
    public Result removeBatch(String ids, String username) {
        System.out.println(ids);
        System.out.println(username);
        Result result = new Result();
        String[] idArray = ids.split(",");
        User user = userService.query(username);
        String statu = user.getStatu();
        if (Integer.parseInt(statu) != 0 && Integer.parseInt(statu) != 1) {
            for (String id : idArray) {
                long i = Long.parseLong(id);
                Order order = orderMapper.selectByPrimaryKey(id);
                Date createTime = order.getCreateTime();
                long createTimeTime = createTime.getTime();
                Date endTime = order.getEndTime();
                long endTimeTime = endTime.getTime();
                Date date = new Date();     //获取当前时间
                Calendar calendar = Calendar.getInstance();
                long timeInMillis = calendar.getTimeInMillis(); //当前时间毫秒值

                if (timeInMillis > endTimeTime) {
                    orderMapper.deleteByPrimaryKey(id);
                }
                result.setFlag(true);
                result.setMessage("已结束的会议删除成功");
            }
        } else {
            result.setFlag(false);
            result.setMessage("对不起，你不是管理员，无法进行此操作！");
        }
        return result;
    }

}
