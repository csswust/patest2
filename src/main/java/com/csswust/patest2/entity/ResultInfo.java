package com.csswust.patest2.entity;

import java.util.Date;

public class ResultInfo {
    private Integer resuId;

    private String name;

    private String shortName;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    public Integer getResuId() {
        return resuId;
    }

    public void setResuId(Integer resuId) {
        this.resuId = resuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
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