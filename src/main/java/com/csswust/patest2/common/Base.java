package com.csswust.patest2.common;

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
}
