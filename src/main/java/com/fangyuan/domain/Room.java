package com.fangyuan.domain;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "room")
public class Room {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String roomname;
    private String roomnum;
    private Integer capacity;
    private String description;
    private String address;
    private String statu;
    @Transient
    private String username;
}
