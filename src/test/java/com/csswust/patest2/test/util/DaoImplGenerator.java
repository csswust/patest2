package com.csswust.patest2.test.util;

import java.util.Map;

/**
 * Created by 972536780 on 2017/11/20.
 */
public class DaoImplGenerator {
    public static void generator(Model model) {
        Map<String, Object> map = Generator.getTemplateMap(model);
        String typeName = (String) map.get("typeName");
        String dIdProperty = toUpperCaseFirstOne(model.getIdProperty());
        map.put("dIdProperty", dIdProperty);
        Generator.writeTemplate(Generator.daoImplPath + "/" + typeName + "DaoImpl.java",
                "mapper-template/DaoImplMapper.ftl", map);
        Generator.writeTemplate(Generator.daoPath + "/" + typeName + "Dao.java",
                "mapper-template/DaoMapper.ftl", map);
    }

    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
