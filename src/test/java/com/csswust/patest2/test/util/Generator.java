package com.csswust.patest2.test.util;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by 972536780 on 2018/4/9.
 */
public class Generator {
    private static String basePath = "E:\\javawork\\patest2\\";
    private static String packagePath = "com\\csswust\\patest2\\";
    public static String basePackage = "com.csswust.patest2";

    public static String modelPath = basePath + "src\\main\\resources\\mybatis_mappers";
    public static String daoPath = basePath + "src\\main\\java\\" + packagePath + "dao";
    public static String actionPath = basePath + "src\\main\\java\\" + packagePath + "controller";
    public static String htmlPath = basePath + "src\\main\\webapp\\index.html";

    public static String daoImplPath = daoPath + "\\impl";
    public static String xmlImplPath = modelPath + "\\impl";
    public static String xmlCustomPath = modelPath + "\\custom";

    private static boolean isOver = false;


    public static void main(String[] args) throws IOException {
        File oldPathFile = new File(modelPath + "\\auto");
        String[] fileList = new String[]{
                "ExamInfoMapper.xml"
        };
        File[] files = oldPathFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            if (fileList.length != 0 && !Arrays.asList(fileList).contains(file.getName())) {
                continue;
            }
            Model model = Generator.getModel(file);
            System.out.println(JSON.toJSONString(model));
            // DaoImplGenerator.generator(model);
            MapperImplGenerator.generator(model);
            //ActionGenerator.generator(model);
        }
    }

    public static Model getModel(String xmlPath) {
        File xmlFile = new File(xmlPath);
        return getModel(xmlFile);
    }

    public static Model getModel(File xmlFile) {
        if (xmlFile.isDirectory()) return null;
        Element rootElement = readXml(xmlFile.getPath());
        Model model = getModel(rootElement);
        return model;
    }

    public static Map<String, Object> getTemplateMap(Model model) {
        String typeName = model.getTypeClass().getSimpleName();
        Map<String, Object> map = new HashMap<>();
        map.put("basePackage", basePackage);
        map.put("typeName", typeName);
        map.put("model", model);
        map.put("isBlob", model.isBlob() ? 1 : 0);
        return map;
    }

    public static void writeTemplate(String writePath, String TemplatePath, Map<String, Object> map) {
        File newFile = new File(writePath);
        if (newFile.exists()) {
            if (isOver) newFile.delete();
            else return;
        }
        writeTemplate(newFile, TemplatePath, map);
    }

    public static void writeTemplate(File writeFile, String TemplatePath, Map<String, Object> map) {
        String template = FileUtil.readResource(TemplatePath);
        if (template == null) return;
        String TemplateContent = FreeMarkerUtil.proccessTemplate(map.toString(),
                template, map);
        if (TemplateContent == null) return;
        try {
            if (!writeFile.exists()) writeFile.createNewFile();
            FileUtils.write(writeFile, TemplateContent, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Model getModel(Element rootElement) {
        Model model = new Model();
        model.setNamespace(rootElement.attributeValue("namespace"));
        List<Element> resultMapList = rootElement.elements("resultMap");
        Element firltMap = resultMapList.get(0);
        // 设置type
        model.setType(firltMap.attributeValue("type"));
        // 获取对应class
        try {
            Class<?> cla = Class.forName(model.getType());
            model.setTypeClass(cla);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 设置主键
        Element id = firltMap.element("id");
        model.setIdColumn(id.attributeValue("column"));
        model.setIdProperty(id.attributeValue("property"));
        model.setIdJdbcType(id.attributeValue("jdbcType"));
        // 设置字段
        model.setFieldList(getField(firltMap));
        if (resultMapList.size() != 1) {
            model.setBlob(true);
            model.setBlobFieldList(getField(resultMapList.get(1)));
        }
        Element delete = rootElement.element("delete");
        String text = delete.getText().replaceAll("\n", "");
        String result = text.split("\\s+")[3];
        model.setTableName(result);
        return model;
    }

    private static List<Field> getField(Element rootElement) {
        List<Field> fieldList = new ArrayList<>();
        List<Element> elementList = rootElement.elements("result");
        for (Element element : elementList) {
            Field field = new Field();
            field.setColumn(element.attributeValue("column"));
            field.setProperty(element.attributeValue("property"));
            field.setJdbcType(element.attributeValue("jdbcType"));
            fieldList.add(field);
        }
        return fieldList;
    }

    private static Element readXml(String filePath) {
        InputStream in = null;
        Element rootElement = null;
        try {
            SAXReader reader = new SAXReader();
            in = new FileInputStream(new File(filePath));
            Document doc = reader.read(in);
            rootElement = doc.getRootElement();
            System.out.println("XMLUtil.readXml root name:" + rootElement.getName());
        } catch (Exception e) {
            System.err.println("XMLUtil.readXml error: " + e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rootElement;
    }
}
