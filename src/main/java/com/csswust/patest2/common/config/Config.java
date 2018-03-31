package com.csswust.patest2.common.config;

import com.csswust.patest2.dao.SiteInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.SiteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 972536780 on 2017/11/20.
 */
public class Config {
    private static Logger log = LoggerFactory.getLogger(Config.class);
    private static volatile Map<String, String> configFile = new ConcurrentHashMap<>();

    // 刷新配置文件的配置
    public static void refreshConfig() {
        Properties prop = new Properties();
        try {
            String path = Config.class.getClassLoader().getResource("config.properties").getPath();
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            prop.load(in);
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                configFile.put(key, prop.getProperty(key));
            }
            in.close();
        } catch (Exception e) {
            log.error("读取config.properties出错 error: {}", e);
        }
    }

    // 刷新siteInfo中的信息，siteInfo优先于config
    public static void refreshSiteInfo(SiteInfoDao siteInfoDao, Integer siteId) {
        try {
            SiteInfo siteInfo = new SiteInfo();
            if (siteId != null) siteInfo.setSiteId(siteId);
            List<SiteInfo> siteInfoList = siteInfoDao.selectByCondition(siteInfo, new BaseQuery());
            if (siteInfoList == null || siteInfoList.size() == 0) return;
            for (SiteInfo item : siteInfoList) {
                if (item == null) continue;
                if (item.getName() == null) continue;
                if (item.getValue() == null) continue;
                configFile.put(item.getName(), item.getValue());
            }
        } catch (Exception e) {
            log.error("读取SiteInfo出错 error: {}", e);
        }
    }

/*    public static String get(String key) {
        return configFile.get(key);
    }*/

    public static String get(String key, String defaultValue) {
        String result = configFile.get(key);
        return result == null ? defaultValue : result;
    }

/*    public static int getToInt(String key) {
        try {
            return Integer.parseInt(configFile.get(key));
        } catch (Exception e) {
            return 0;
        }
    }*/

    public static int getToInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(configFile.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

/*    public static long getToLong(String key) {
        try {
            return Long.parseLong(configFile.get(key));
        } catch (Exception e) {
            return 0;
        }
    }*/

    public static long getToLong(String key, long defaultValue) {
        try {
            return Long.parseLong(configFile.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
