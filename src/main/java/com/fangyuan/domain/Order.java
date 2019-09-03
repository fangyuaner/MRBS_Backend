package com.fangyuan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Table(name = "orders")
public class Order {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String meettingname;
    private Long roomId;
    private Long userId;
    @Transient
    private String roomname;
    @Transient
    private String username;
    private String capacity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String description;

    private String statu;
    @Transient
    private String name;
}
