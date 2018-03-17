package com.csswust.patest2.dao.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2017/11/26.
 */
public class BaseQuery {
    private Integer page;
    private Integer rows;
    private Map<String, Object> customParam = new HashMap<>();

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

    public void setPageRows(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

    public Map<String, Object> getCustomParam() {
        return customParam;
    }

    public void setCustomParam(Map<String, Object> customParam) {
        this.customParam = customParam;
    }

    public void setCustom(String key, Object value) {
        customParam.put(key, value);
    }
}
