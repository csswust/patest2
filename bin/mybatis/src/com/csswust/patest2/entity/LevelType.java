package com.csswust.patest2.entity;

import java.util.Date;

public class LevelType {
    private Integer levTypId;

    private String name;

    private Date createTime;

    private Date modifyTime;

    public Integer getLevTypId() {
        return levTypId;
    }

    public void setLevTypId(Integer levTypId) {
        this.levTypId = levTypId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}