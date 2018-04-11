package com.csswust.patest2.entity;

import java.util.Date;

public class JudgerInfo {
    private Integer judId;

    private String name;

    private String repr;

    private String fileName;

    private String execFileName;

    private String suffix;

    private Integer enabled;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    public Integer getJudId() {
        return judId;
    }

    public void setJudId(Integer judId) {
        this.judId = judId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRepr() {
        return repr;
    }

    public void setRepr(String repr) {
        this.repr = repr == null ? null : repr.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getExecFileName() {
        return execFileName;
    }

    public void setExecFileName(String execFileName) {
        this.execFileName = execFileName == null ? null : execFileName.trim();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix == null ? null : suffix.trim();
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
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