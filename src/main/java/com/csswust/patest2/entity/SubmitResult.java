package com.csswust.patest2.entity;

import java.util.Date;

public class SubmitResult {
    private Integer subResId;

    private Integer submitId;

    private Integer testId;

    private Integer status;

    private Integer usedTime;

    private Integer usedMemory;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    private String errMsg;

    public SubmitResult(){}

    public SubmitResult(Integer submitId, Integer testId, Integer status, Integer usedTime, Integer usedMemory, String errMsg) {
        this.submitId = submitId;
        this.testId = testId;
        this.status = status;
        this.usedTime = usedTime;
        this.usedMemory = usedMemory;
        this.errMsg = errMsg;
    }

    public Integer getSubResId() {
        return subResId;
    }

    public void setSubResId(Integer subResId) {
        this.subResId = subResId;
    }

    public Integer getSubmitId() {
        return submitId;
    }

    public void setSubmitId(Integer submitId) {
        this.submitId = submitId;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    public Integer getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Integer usedMemory) {
        this.usedMemory = usedMemory;
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

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg == null ? null : errMsg.trim();
    }
}