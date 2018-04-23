package com.csswust.patest2.service.monitor;

import com.csswust.patest2.common.APIResult;
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
    private int number = 20;

    public List<MonitorRe> getDataByKey(String key, long timeUnit) {
        APIResult apiResult = new APIResult();
        refresh(key);
        List<MonitorBase> list = data.get(key);
        if (list == null) return null;
        long nowTime = new Date().getTime();
        long startTime = nowTime - (timeUnit * 1000 * number);
        List<MonitorRe> monitorReList = new ArrayList<>(number + 2);
        for (int i = 0; i < number; i++) {
            MonitorRe monitorRe = new MonitorRe();
            monitorRe.setData(0);
            monitorRe.setDate(new Date(startTime + i * timeUnit * 1000));
            monitorReList.add(monitorRe);
        }
        int length = list.size();
        for (int i = length - 1; i >= 0; i--) {
            MonitorBase base = list.get(i);
            if (base == null || base.getCurrTime() == null) continue;
            long basetime = base.getCurrTime().getTime();
            if (basetime < startTime) break;
            long chaTime = basetime - startTime;
            int index = (int) (chaTime / (timeUnit * 1000));
            if (index < 0 || index >= number) continue;
            MonitorRe monitorRe = monitorReList.get(index);
            monitorRe.setData(monitorRe.getData() + base.getData());
        }
        return monitorReList;
    }

    private void refresh(String key) {
        List<MonitorBase> list = data.get(key);
        if (list == null) {
            list = new LinkedList<>();
            data.put(key, list);
        }
        int removeCount = monitor.removeAll(key, list);
        log.info("Monitor.removeAll key: {}, count: {}", key, removeCount);
        int deleteCount = deleteExpiredData(list);
        log.info("MonitorService.deleteExpiredData key: {}, count: {}", key, deleteCount);
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
