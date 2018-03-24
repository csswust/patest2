package com.csswust.patest2.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class IntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        // 判断是否是" ",或者"     "之类的字符串
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            return Integer.parseInt(source);
        } catch (Exception e) {
            return null;
        }
    }
}
