package com.csswust.patest2.test.util;

/**
 * Created by 972536780 on 2018/3/13.
 */
public class Field {
    private String column;
    private String property;
    private String jdbcType;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}
