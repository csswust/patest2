package com.csswust.patest2.entity;

import java.util.Date;

public class UserLoginLog {
    private Integer useLogId;

    private Integer userId;

    private String loginIp;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    public Integer getUseLogId() {
        return useLogId;
    }

    public void setUseLogId(Integer useLogId) {
        this.useLogId = useLogId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
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