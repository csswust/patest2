package com.csswust.patest2.entity;

import java.util.Date;

public class UserRole {
    private Integer useRolId;

    private String name;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    public Integer getUseRolId() {
        return useRolId;
    }

    public void setUseRolId(Integer useRolId) {
        this.useRolId = useRolId;
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

    public Integer getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }
}