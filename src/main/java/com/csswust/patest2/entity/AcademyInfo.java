package com.csswust.patest2.entity;

import java.util.Date;

public class AcademyInfo {
    private Integer acaId;

    private String academyName;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    public Integer getAcaId() {
        return acaId;
    }

    public void setAcaId(Integer acaId) {
        this.acaId = acaId;
    }

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName == null ? null : academyName.trim();
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