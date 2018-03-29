package com.csswust.patest2.common.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by 972536780 on 2017/11/20.
 */
public class Config {
    private static volatile Map<String, String> configFile;

    static {
        refresh();
    }

    public static void refresh() {
        Properties prop = new Properties();
        Map<String, String> copy = new HashMap<>();
        try {
            String path = Config.class.getClassLoader().getResource("config.properties").getPath();
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            prop.load(in);
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                configFile.put(key, prop.getProperty(key));
            }
            configFile = copy;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return configFile.get(key);
    }

    public static String get(String key, String defaultValue) {
        String result = configFile.get(key);
        return result == null ? defaultValue : result;
    }

    public static int getToInt(String key) {
        try {
            return Integer.parseInt(configFile.get(key));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getToInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(configFile.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
