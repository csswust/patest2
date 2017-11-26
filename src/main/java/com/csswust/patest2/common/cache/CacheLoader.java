package com.csswust.patest2.common.cache;

/**
 * Created by 972536780 on 2017/11/26.
 */
public interface CacheLoader<K, V> {
    V load(K key) throws Exception;
}
