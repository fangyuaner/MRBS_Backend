package com.fangyuan.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Result {
    private boolean flag;
    private String message;
    private List<?> list;
    private Map<String,List<?>> map;
    private int encode;

    public Result(String message, int encode) {
        this.message = message;
        this.encode = encode;
    }

    public Result(Map<String, List<?>> map) {
        this.map = map;
    }

    public Result(String message, List<?> list) {
        this.message = message;
        this.list = list;
    }

    private Object obj;

    public Result(boolean flag, String message, Object obj) {
        this.flag = flag;
        this.message = message;
        this.obj = obj;
    }

    public Result() {

    }

    public Result(boolean flag, String message, List<?> list, Object obj) {
        this.flag = flag;
        this.message = message;
        this.list = list;
        this.obj = obj;
    }

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }
}
