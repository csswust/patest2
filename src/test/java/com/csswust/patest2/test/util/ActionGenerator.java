package com.csswust.patest2.test.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by 972536780 on 2018/4/9.
 */
public class ActionGenerator {
    public static void generator(Model model) {
        Map<String, Object> map = Generator.getTemplateMap(model);
        String typeName = (String) map.get("typeName");
        String lTypeName = toLowerCaseFirstOne(typeName);
        map.put("lTypeName", lTypeName);
        Generator.writeTemplate(Generator.actionPath + "/" + typeName + "Action.java",
                "mapper-template/Action.ftl", map);
        writeHtml(lTypeName);
    }

    private static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    private static void writeHtml(String s) {
        try {
            String str = FileUtils.readFileToString(new File(Generator.htmlPath), "UTF-8");
            int index = str.indexOf("</body>");
            StringBuilder builder = new StringBuilder(str);
            String result = String.format("<a href=\"./%s/selectByCondition\" target=\"_blank\">查询%s</a></br>\n", s, s);
            builder.insert(index, result);
            FileUtils.write(new File(Generator.htmlPath), builder.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
