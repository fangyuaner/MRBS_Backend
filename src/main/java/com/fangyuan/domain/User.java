package com.fangyuan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String realname;
    private String username;
    //@JsonIgnore
    private String password;
    private String grade;
    private String sex;
    private String phone;
    private String email;
    private String statu;
}
