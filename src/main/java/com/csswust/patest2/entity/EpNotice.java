package com.csswust.patest2.entity;

import java.util.Date;

public class EpNotice {
    private Integer epnoId;

    private String title;

    private Date createTime;

    private Date modifyTime;

    private Integer modifyUserId;

    private String content;

    public Integer getEpnoId() {
        return epnoId;
    }

    public void setEpnoId(Integer epnoId) {
        this.epnoId = epnoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}