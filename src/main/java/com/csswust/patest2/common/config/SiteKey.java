package com.csswust.patest2.common.config;

/**
 * Created by 972536780 on 2017/11/20.
 */
public class SiteKey {
    // 系统运行根目录
    public final static String PATEST_WORK_PATH = "patest_work_path";
    public final static String PATEST_WORK_PATH_DE = "/home/mryang/patest/patest2";

    // cache本地缓存获取结果的超时时间，单位ms
    public final static String CACHE_GET_TIMEOUT = "cache_get_timeout";
    public final static int CACHE_GET_TIMEOUT_DE = 10000;

    // 上传临时目录
    public final static String UPLOAD_TEMP_DIR = "upload_temp_dir";
    public final static String UPLOAD_TEMP_DIR_DE = PATEST_WORK_PATH_DE + "/temp";

    // 	ueditor上传目录
    public final static String UPLOAD_UEDITOR_DIR = "upload_ueditor_dir";
    public final static String UPLOAD_UEDITOR_DIR_DE = PATEST_WORK_PATH_DE + "/ueditor";

    // 	判题最大线程数
    public final static String JUDGE_THREAD_POOL_NUM = "judge_thread_pool_num";
    public final static int JUDGE_THREAD_POOL_NUM_DE = 5;

    // 判题任务队列大小
    public final static String JUDGE_TASK_QUEUE_TOTAL = "judge_task_queue_total";
    public final static int JUDGE_TASK_QUEUE_TOTAL_DE = 100000;

    // 判题时单个数据最大内存 KB
    public final static String JUDGE_MAX_LIMIT_MEMORY = "judge_max_limit_memory";
    public final static int JUDGE_MAX_LIMIT_MEMORY_DE = 102400;

    // 判题时单个数据最大时间 单位s
    public final static String JUDGE_MAX_LIMIT_TIME = "judge_max_limit_time";
    public final static int JUDGE_MAX_LIMIT_TIME_DE = 10000;

    // 最大测试组数
    public final static String JUDGE_MAX_TEST_NUM = "judge_max_test_num";
    public final static int JUDGE_MAX_TEST_NUM_DE = 30;

    // 允许运行的语言
    public final static String JUDGE_ALLOW_LANGUAGE = "judge_allow_language";
    public final static String JUDGE_ALLOW_LANGUAGE_DE = "1,2,3";

    // 判题任务最多执行的时间，秒
    public final static String JUDGE_MAX_RUN_TIME = "judge_max_run_time";
    public final static int JUDGE_MAX_RUN_TIME_DE = 60;

    // judgeWork目录
    public final static String JUDGE_WORK_DIR = "judge_work_dir";
    public final static String JUDGE_WORK_DIR_DE = PATEST_WORK_PATH_DE + "/judge/work";

    // 	是否删除工作目录,如果设置为0，那么要定期删除
    public final static String JUDGE_IS_DELETE_DIR = "judge_is_delete_dir";
    public final static int JUDGE_IS_DELETE_DIR_DE = 0;

    // 脚本文件地址
    public final static String JUDGE_SCRIPT_PATH = "judge_script_path";
    public final static String JUDGE_SCRIPT_PATH_DE = PATEST_WORK_PATH_DE + "/judge/script/ysf.py";

    // 测试数据目录
    public final static String JUDGE_STANDARD_DATA_PATH = "judge_standard_data_path";
    public final static String JUDGE_STANDARD_DATA_PATH_DE = PATEST_WORK_PATH_DE + "/judge/testdata";

    // special目录
    public final static String JUDGE_SPECIAL_DATA_PATH = "judge_special_data_path";
    public final static String JUDGE_SPECIAL_DATA_PATH_DE = PATEST_WORK_PATH_DE + "/judge/special";

    // 	是否开启登录锁
    public final static String IS_LOGIN_LOCK = "is_login_lock";
    public final static int IS_LOGIN_LOCK_DE = 0;

    // 是否开启ip限制
    public final static String IS_LIMIT_IP = "is_limit_ip";
    public final static int IS_LIMIT_IP_DE = 0;

    // 是否开启权限判断
    public final static String IS_AUTH_JUDGE = "is_auth_judge";
    public final static int IS_AUTH_JUDGE_DE = 1;

    // 重判每次提交任务数
    public final static String REJUDGE_SINGLE_MAX_NUM = "rejudge_single_max_num";
    public final static int REJUDGE_SINGLE_MAX_NUM_DE = 100;

    // 格式错误是否计算分数
    public final static String IS_SCORE_PRESENTATION_ERROR = "is_score_presentation_error";
    public final static int IS_SCORE_PRESENTATION_ERROR_DE = 0;

    // 查看数据最大大小，1MB
    public final static String SELECT_PROBLEM_DATA_MAX = "select_problem_data_max";
    public final static int SELECT_PROBLEM_DATA_MAX_DE = 1048576;

    // 两次提交最大时间间隔
    public final static String SUBMIT_MAX_TIME_INTERVAL = "submit_max_time_interval";
    public final static int SUBMIT_MAX_TIME_INTERVAL_DE = 30000;

    // 保留一天内的监控数据
    public final static String MONITOR_TIME_INTERVAL = "monitor_time_interval";
    public final static long MONITOR_TIME_INTERVAL_DE = 1000 * 60 * 60 * 24;

    // 监控数据队列大小最大值
    public final static String MONITOR_DATA_MAX_NUMBER = "monitor_data_max_number";
    public final static int MONITOR_DATA_MAX_NUMBER_DE = 1000000;
}