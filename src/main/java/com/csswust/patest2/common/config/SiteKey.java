package com.csswust.patest2.common.config;

/**
 * Created by 972536780 on 2017/11/20.
 */
public class SiteKey {
    // cache本地缓存获取结果的超时时间，单位ms
    public final static String CACHE_GET_TIMEOUT = "cache_get_timeout";
    public final static int CACHE_GET_TIMEOUT_DEFAULT = 10000;

    // temp
    public final static String UPLOAD_TEMP_DIR = "upload_temp_dir";
    // ueditor
    public final static String UPLOAD_UEDITOR_DIR = "upload_ueditor_dir";

    // judge
    public final static String JUDGE_TASK_QUEUE_TOTAL = "judge_task_queue_total";
    public final static String JUDGE_THREAD_POOL_NUM = "judge_thread_pool_num";
    public final static String JUDGE_MAX_LIMIT_MEMORY = "judge_max_limit_memory";
    public final static String JUDGE_MAX_LIMIT_TIME = "judge_max_limit_time";
    public final static String JUDGE_MAX_TEST_NUM = "judge_max_test_num";
    public final static String JUDGE_ALLOW_LANGUAGE = "judge_allow_language";
    public final static String JUDGE_MAX_RUN_TIME = "judge_max_run_time";

    public final static String JUDGE_WORK_DIR = "judge_work_dir";
    public final static String JUDGE_IS_DELETE_DIR = "judge_is_delete_dir";
    public final static String JUDGE_SCRIPT_PATH = "judge_script_path";
    public final static String JUDGE_STANDARD_DATA_PATH = "judge_standard_data_path";
    public final static String JUDGE_SPECIAL_DATA_PATH = "judge_special_data_path";
    public final static String JUDGE_JAVA_FILE_NAME = "judge_java_file_Name";
    public final static String JUDGE_GCC_FILE_NAME = "judge_gcc_file_name";
    public final static String JUDGE_GPP_FILE_NAME = "judge_gpp_file_name";
    public final static String JUDGE_PYTHON_FILE_NAME = "judge_python_file_name";

    // user
    public final static String IS_LOGIN_LOCK = "is_login_lock";
    public final static String IS_LIMIT_IP = "is_limit_ip";
    public final static String IS_AUTH_JUDGE = "is_auth_judge";

    public final static String REJUDGE_SINGLE_NUM = "rejudge_single_num";
    public final static String REJUDGE_WAIT_TIME = "rejudge_wait_time";
    public final static String REJUDGE_TASK_QUEUE_TOTAL = "rejudge_task_queue_total";
}
