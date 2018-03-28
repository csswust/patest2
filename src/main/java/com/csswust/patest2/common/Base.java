package com.csswust.patest2.common;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 972536780 on 2018/3/16.
 */
public class Base {
    private static Logger log = LoggerFactory.getLogger(Base.class);

    public Integer StringToInt(String str) {
        Integer result = null;
        try {
            result = Integer.parseInt(str);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public String format(String format, Object... args) {
        return String.format(format, args);
    }

    public String getFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
        Matcher matcher = pattern.matcher(fileName);
        fileName = matcher.replaceAll(""); // 将匹配到的非法字符以空替换
        return fileName;
    }

    public String getJson(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.error("JSON.toJSONString error: {}", e);
        }
        return "";
    }

    public static String getPath(String key) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String time = sdf.format(new Date());
        return Config.get(key) + "/" + time;
    }

    public static void main(String[] args) {
        System.out.println(getPath(SiteKey.UPLOAD_TEMP_DIR));
    }
}
