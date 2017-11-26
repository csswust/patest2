package com.csswust.patest2.common.cache;

import com.csswust.patest2.common.config.Config;

import static com.csswust.patest2.common.config.SiteKey.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by 972536780 on 2017/11/26.
 */
public class Cache<K, V> {
    private static Logger log = LoggerFactory.getLogger(Cache.class);

    private Map<K, FutureTask<V>> map = new ConcurrentHashMap<>();
    private CacheLoader<K, V> cacheLoader;

    public Cache(CacheLoader<K, V> cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    public V get(K key) {
        if (!map.containsKey(key)) {
            FutureTask<V> task = new FutureTask<V>(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return cacheLoader.load(key);
                }
            });
            // 如果不存在则put
            FutureTask<V> temp = map.putIfAbsent(key, task);
            if (temp == null) {
                task.run();
            }
        }
        FutureTask<V> task = map.get(key);
        try {
            return task.get(Config.getToInt(CACHE_GET_TIMEOUT, CACHE_GET_TIMEOUT_DEFAULT), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("cache get error InterruptedException: {}", e);
        } catch (ExecutionException e) {
            log.error("cache get error ExecutionException: {}", e);
        } catch (TimeoutException e) {
            log.error("cache get error TimeoutException: {}", e);
        }
        return null;
    }
}
