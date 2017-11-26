package com.csswust.patest2.entity;

import java.util.Date;

public class VisitPath {
    private Integer visPatId;

    private String url;

    private String description;

    private String roleIds;

    private Date createTime;

    private Date modifyTime;

    public Integer getVisPatId() {
        return visPatId;
    }

    public void setVisPatId(Integer visPatId) {
        this.visPatId = visPatId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds == null ? null : roleIds.trim();
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