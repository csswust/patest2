package com.csswust.patest2.common;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 972536780 on 2018/3/16.
 */
public class Base {
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
}
