package com.csswust.patest2.service.monitor;

import com.csswust.patest2.service.common.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by 972536780 on 2018/4/23.
 */
@Component
public class Monitor extends BaseService {
    private static Logger log = LoggerFactory.getLogger(Monitor.class);

    public Map<String, Queue<MonitorBase>> data = new HashMap<>();
    public Map<String, MonitorType> typeMap = new HashMap<>();

    public boolean addCount(String key, int count) {
        Queue<MonitorBase> queue = getQueue(key, MonitorType.count);
        if (queue == null) return false;
        MonitorCount monitorCount = new MonitorCount(count, new Date());
        return queue.offer(monitorCount);
    }

    public boolean addSize(String key, int size) {
        Queue<MonitorBase> queue = getQueue(key, MonitorType.size);
        if (queue == null) return false;
        MonitorSize monitorSize = new MonitorSize(size, new Date());
        return queue.offer(monitorSize);
    }

    public int removeAll(String key, Collection<MonitorBase> collection) {
        Queue<MonitorBase> queue = getQueue(key, null);
        if (queue == null) return 0;
        int count = 0;
        while (true) {
            MonitorBase base = queue.poll();
            if (base == null) break;
            count++;
            collection.add(base);
        }
        return count;
    }

    public MonitorType judgeType(String key) {
        return typeMap.get(key);
    }

    private Queue<MonitorBase> getQueue(String key, MonitorType type) {
        Queue<MonitorBase> queue = data.get(key);
        if (queue == null) {
            if (type == null) return null;
            queue = new ArrayBlockingQueue<>(10000);
            data.put(key, queue);
            typeMap.put(key, type);
        }
        return queue;
    }
}
