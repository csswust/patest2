package com.csswust.patest2.service.monitor;

import java.util.Date;

/**
 * Created by 972536780 on 2018/4/23.
 */
public class MonitorCount implements MonitorBase {
    private Integer count;
    private Date nowTime;

    public MonitorCount() {
    }

    public MonitorCount(Integer count, Date nowTime) {
        this.count = count;
        this.nowTime = nowTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
        return count;
    }
}
