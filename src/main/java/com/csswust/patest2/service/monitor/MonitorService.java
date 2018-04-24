package com.csswust.patest2.service.monitor;

import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.service.common.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 972536780 on 2018/4/23.
 */
@Service
public class MonitorService extends BaseService {
    private static Logger log = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private Monitor monitor;
    private static Map<String, List<MonitorBase>> data = new HashMap<>();

    public List<MonitorRe> getDataByKey(String key, int number, long timeUnit) {
        refresh(key);
        List<MonitorBase> list = data.get(key);
        if (list == null) return null;
        timeUnit = timeUnit * 1000;
        long nowTime = getNowTime(timeUnit);
        long startTime = nowTime - (timeUnit * number);
        MonitorType type = monitor.judgeType(key);
        List<MonitorRe> monitorReList = new ArrayList<>(number + 2);
        for (int i = 0; i < number; i++) {
            MonitorRe monitorRe = new MonitorRe();
            monitorRe.setData(0);
            monitorRe.setTempNum(0);
            monitorRe.setDate(new Date(startTime + i * timeUnit));
            monitorReList.add(monitorRe);
        }
        int length = list.size();
        for (int i = length - 1; i >= 0; i--) {
            MonitorBase base = list.get(i);
            if (base == null || base.getCurrTime() == null) continue;
            long basetime = base.getCurrTime().getTime();
            if (basetime < startTime) break;
            long chaTime = basetime - startTime;
            int index = (int) (chaTime / (timeUnit));
            if (index < 0 || index >= number) continue;
            MonitorRe monitorRe = monitorReList.get(index);
            if (type == MonitorType.count) {
                setCountData(monitorRe, base);
            } else if (type == MonitorType.size) {
                setsizeData(monitorRe, base);
            }
        }
        if (type == MonitorType.size) {
            for (int i = 0; i < number; i++) {
                MonitorRe monitorRe = monitorReList.get(i);
                if (monitorRe.getTempNum() == 0) continue;
                monitorRe.setData(monitorRe.getData() / monitorRe.getTempNum());
            }
        }
        return monitorReList;
    }

    private void setCountData(MonitorRe monitorRe, MonitorBase base) {
        monitorRe.setData(monitorRe.getData() + base.getData());
    }

    private void setsizeData(MonitorRe monitorRe, MonitorBase base) {
        monitorRe.setData(monitorRe.getData() + base.getData());
        monitorRe.setTempNum(monitorRe.getTempNum() + 1);
    }

    private long getNowTime(long timeUnit) {
        long nowTime = new Date().getTime();
        return nowTime - nowTime % timeUnit + timeUnit;
    }

    private void refresh(String key) {
        List<MonitorBase> list = data.get(key);
        if (list == null) {
            list = new LinkedList<>();
            data.put(key, list);
        }
        int addCount = monitor.removeAll(key, list);
        log.info("Monitor.addCount key: {}, count: {}", key, addCount);
        int deleteCount = deleteExpiredData(list);
        log.info("MonitorService.deleteExpiredData key: {}, count: {}", key, deleteCount);
        int maxNum = Config.getToInt(SiteKey.MONITOR_DATA_MAX_NUMBER,
                SiteKey.MONITOR_DATA_MAX_NUMBER_DE);
        if (list.size() > maxNum) {
            int removeBynum = removeBynum(list, list.size() - maxNum);
            log.info("MonitorService.removeBynum key: {}, count: {}", key, removeBynum);
        }
    }

    private int removeBynum(List<MonitorBase> list, int num) {
        Iterator<MonitorBase> it = list.iterator();
        int count = 0;
        while (it.hasNext()) {
            it.next();
            it.remove();
            count++;
            if (count == num) break;
        }
        return count;
    }

    private int deleteExpiredData(List<MonitorBase> list) {
        Iterator<MonitorBase> it = list.iterator();
        long nowTime = System.currentTimeMillis();
        long mo_interval_time = Config.getToLong(
                SiteKey.MONITOR_TIME_INTERVAL, SiteKey.MONITOR_TIME_INTERVAL_DE);
        int count = 0;
        while (it.hasNext()) {
            MonitorBase base = it.next();
            Date time = base.getCurrTime();
            if (time == null) continue;
            if (time.getTime() - nowTime > mo_interval_time) it.remove();
            else break;
            count++;
        }
        return count;
    }
}
