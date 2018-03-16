package com.csswust.patest2.common.config;

/**
 * Created by 972536780 on 2017/11/20.
 */
public class SiteKey {
    // 废弃
    public final static String AUTH_TABLE_LOADING_INTERVAL = "auth.table.loading.interval";
    public final static int AUTH_TABLE_LOADING_INTERVAL_DEFAULT = 100000;

    // cache本地缓存获取结果的超时时间，单位ms
    public final static String CACHE_GET_TIMEOUT = "cache_get_timeout";
    public final static int CACHE_GET_TIMEOUT_DEFAULT = 10000;

    public final static String UPLOAD_TEMP_DIR = "upload_temp_dir";
}
