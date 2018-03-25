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

    // temp
    public final static String UPLOAD_TEMP_DIR = "upload_temp_dir";
    public final static String UPLOAD_UEDITOR_DIR = "upload_ueditor_dir";

    // judge
    public final static String JUDGE_SCRIPT_PATH = "judge_script_path";
    public final static String JUDGE_SCRIPT_NAME = "judge_script_name";
    public final static String JUDGE_SOURCE_PATH = "judge_source_path";
    public final static String JUDGE_STANDARD_DATA_PATH = "judge_standard_data_path";
    public final static String JUDGE_SPECIAL_DATA_PATH = "judge_special_data_path";
    public final static String JUDGE_JAVA_FILE_NAME = "judge_java_file_Name";
    public final static String JUDGE_GCC_FILE_NAME = "judge_gcc_file_name";
    public final static String JUDGE_GPP_FILE_NAME = "judge_gpp_file_name";
    public final static String JUDGE_PYTHON_FILE_NAME = "judge_python_file_name";

    public final static String IS_LOGIN_LOCK = "is_login_lock";
    public final static String IS_LIMIT_IP = "is_limit_ip";
    public final static String IS_AUTH_JUDGE = "is_auth_judge";

}
