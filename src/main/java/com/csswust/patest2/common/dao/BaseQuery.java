package com.csswust.patest2.common.dao;

/**
 * Created by 972536780 on 2017/11/26.
 */
public class BaseQuery {
    private Integer page;
    private Integer rows;

    public BaseQuery() {

    }

    public BaseQuery(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
