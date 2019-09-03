package com.fangyuan.service;

import com.fangyuan.domain.Room;
import com.fangyuan.domain.User;
import com.fangyuan.mapper.RoomMapper;
import com.fangyuan.vo.PageResult;
import com.fangyuan.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {
    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    public String findRoomNameByRoomId(Long roomId) {
        Room room = roomMapper.selectByPrimaryKey(roomId);
        return room.getRoomname();
    }

    //查询所有roomname 和 对应的statu
    public Result findAllRoomName(String roomname) {

        //查询条件
        Example example = new Example(Room.class);
        List<Room> roomList = null;
        if (roomname == null || roomname == "") {
            roomList = roomMapper.selectByExample(example);
        } else {
            example.createCriteria().orLike("roomname", "%" + roomname + "%");

            roomList = roomMapper.selectByExample(example);
        }

//        List<Room> roomList = roomMapper.selectAll();

        List<Pair<Long,String>> roomNameList0 = new ArrayList<>();
        List<Pair<Long,String>> roomNameList1 = new ArrayList<>();
        List<Pair<Long,String>> roomNameList2 = new ArrayList<>();
        List<Pair<Long,String>> roomNameList3 = new ArrayList<>();

        HashMap<String, List<?>> map = new HashMap<>();

        for (Room room : roomList) {
            String roomname1 = room.getRoomname();
            Long id = room.getId();
            String statu = room.getStatu();

            if (statu.equals("0")) {
                roomNameList0.add(new Pair<Long, String>(id,roomname1));
            } else if (statu.equals("1")) {
                roomNameList1.add(new Pair<Long, String>(id,roomname1));
            } else if (statu.equals("2")) {
                roomNameList2.add(new Pair<Long, String>(id,roomname1));
            } else {
                roomNameList3.add(new Pair<Long, String>(id,roomname1));
            }
        }
        map.put("小型会议室", roomNameList0);
        map.put("中型会议室", roomNameList1);
        map.put("大型会议室", roomNameList2);
        map.put("超大型会议室", roomNameList3);

        /*for (Room room : roomList) {
            String roomname = room.getRoomname();
            String statu = room.getStatu();
            for (int i = 0; i < 4; i++) {
                if (statu == Integer.toString(i)){
                   List<Object> list = new ArrayList<>();
                }
            }
        }*/
        Result result = new Result();
        result.setMap(map);

        return result;
    }

    //分页查询
    public PageResult<Room> findRoomByPage(Integer page, Integer limit, String roomname) {
        //设置分页
        PageHelper.startPage(page, limit);
        Example example = new Example(Room.class);
        List<Room> roomList = null;

        if (roomname == null || roomname == "") {
            roomList = roomMapper.selectByExample(example);
        } else {
            example.createCriteria().orLike("roomname", "%" + roomname + "%");

            roomList = roomMapper.selectByExample(example);
        }
        PageInfo<Room> pageInfo = new PageInfo<>(roomList);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());

    }

    //更新
    public Result update(Room room) {
        Result result = new Result();
        String username = room.getUsername();
        User user = userService.query(username);
        String statu = user.getStatu();

        if (Integer.parseInt(statu) != 0 && Integer.parseInt(statu) != 1) {
            Integer capacity = room.getCapacity();
            if (capacity <= 20) {
                room.setStatu("0"); //小于20人，小型
            } else if (capacity <= 40) {
                room.setStatu("1"); //大于20小于40，中型
            } else if (capacity <= 80) {
                room.setStatu("2"); //大于40小于80，大型
            } else {
                room.setStatu("3"); //大于80 ，超大型
            }
            roomMapper.updateByPrimaryKey(room);
            result.setFlag(true);
            result.setMessage("修改成功");
        } else {
            result.setFlag(false);
            result.setMessage("对不起，您不是管理员，无法进行该操作！");
        }
        return result;
    }

    //单个删除
    public Result remove(Long id, String username) {
        Result result = new Result();

        User user = userService.query(username);
        String statu = user.getStatu();
        System.out.println(statu);

        if (Integer.parseInt(statu) != 0 && Integer.parseInt(statu) != 1) {

            roomMapper.deleteByPrimaryKey(id);
            result.setFlag(true);
            result.setMessage("删除成功！");
        } else {
            result.setFlag(false);
            result.setMessage("对不起，你不是管理员，无法进行此操作！");
        }
        return result;
    }

    //新增
    public Result add(Room room) {

        Result result = new Result();
        String username = room.getUsername();
        User user = userService.query(username);
        String statu = user.getStatu();
        if (Integer.parseInt(statu) != 0 && Integer.parseInt(statu) != 1) {
            Integer roomCapacity = room.getCapacity();
            int capacity = roomCapacity.intValue();
            if (capacity <= 20) {
                room.setStatu("0"); //小于20人，小型
            } else if (capacity <= 40) {
                room.setStatu("1"); //大于20小于40，中型
            } else if (capacity <= 80) {
                room.setStatu("2"); //大于40小于80，大型
            } else {
                room.setStatu("3"); //大于80 ，超大型
            }
            roomMapper.insert(room);
            result.setMessage("新增成功！");
            result.setFlag(true);
        } else {
            result.setMessage("对不起，你不是管理无权进行该操作！");
            result.setFlag(false);
        }
        return result;
    }

    //批量删除
    public Result removeBatch(String ids, String username) {
        Result result = new Result();
        String[] idArray = ids.split(",");
        User user = userService.query(username);
        String statu = user.getStatu();

        if (Integer.parseInt(statu) != 0 && Integer.parseInt(statu) != 1) {
            for (String id : idArray) {
                int i = Integer.parseInt(id);
                roomMapper.deleteByPrimaryKey(i);
                result.setFlag(true);
                result.setMessage("删除成功！");
            }
        } else {
            result.setFlag(false);
            result.setMessage("对不起，你不是管理员，无法进行此操作！");
        }

        return result;
    }

    public int findRoomCapById(Long roomId) {
        Room room = roomMapper.selectByPrimaryKey(roomId);
        return room.getCapacity();
    }

    public Room findRoomById(Long id) {
        return roomMapper.selectByPrimaryKey(id);

    }
}
