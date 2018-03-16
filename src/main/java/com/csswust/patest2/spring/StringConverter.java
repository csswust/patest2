package com.csswust.patest2.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        if (source != null) {
            source = source.trim();
        }
        if (StringUtils.isBlank(source)) {
            return null;
        } else {
            return source;
        }
    }
}
