package com.csswust.patest2.spring;

import org.springframework.core.convert.converter.Converter;

public class IntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        try {
            return Integer.parseInt(source);
        } catch (Exception e) {
            return null;
        }
    }
}
