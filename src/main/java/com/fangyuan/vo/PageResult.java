package com.fangyuan.vo;

import lombok.Data;

import java.util.List;
@Data
public class PageResult<T> {

    //需要总条数、总页数、当前页数据
    private Long total;
    private Integer totalPage;
    private List<T> items;

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult() {

    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
