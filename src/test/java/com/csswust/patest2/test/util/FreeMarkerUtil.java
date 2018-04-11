package com.csswust.patest2.test.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;

public class FreeMarkerUtil {
    private final static Configuration cfg = new Configuration();

    static {
        cfg.setDefaultEncoding("UTF-8");
    }

    /**
     * 渲染模板
     */
    public static String proccessTemplate(String id, String stringTemplate, Object params) {
        Template template = null;
        try {
            template = new Template(id, new StringReader(stringTemplate), cfg);
            StringWriter writer = new StringWriter();
            template.process(params, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
