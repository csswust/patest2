package com.csswust.patest2.service.monitor;

import java.util.Date;

/**
 * Created by 972536780 on 2018/4/23.
 */
public class MonitorSize implements MonitorBase {
    private Integer size;
    private Date nowTime;

    public MonitorSize() {
    }

    public MonitorSize(Integer size, Date nowTime) {
        this.size = size;
        this.nowTime = nowTime;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    @Override
    public Date getCurrTime() {
        return nowTime;
    }

    @Override
    public int getData() {
        return size;
    }
}
