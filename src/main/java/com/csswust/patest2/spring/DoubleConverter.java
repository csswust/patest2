package com.csswust.patest2.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DoubleConverter implements Converter<String, Double> {
    @Override
    public Double convert(String source) {
        // 判断是否是" ",或者"     "之类的字符串
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            return Double.parseDouble(source);
        } catch (Exception e) {
            return null;
        }
    }
}
