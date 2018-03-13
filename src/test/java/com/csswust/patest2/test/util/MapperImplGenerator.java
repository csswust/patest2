package com.csswust.patest2.test.util;

import com.alibaba.fastjson.JSON;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by 972536780 on 2018/3/13.
 */

class Field {
    private String column;
    private String property;
    private String jdbcType;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}

class Model {
    private String namespace;
    private String tableName;
    private String type;
    private String idColumn;
    private String idProperty;
    private String idJdbcType;
    private List<Field> fieldList = new ArrayList<>();
    private boolean isBlob = false;
    private List<Field> blobFieldList = new ArrayList<>();

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public String getIdJdbcType() {
        return idJdbcType;
    }

    public void setIdJdbcType(String idJdbcType) {
        this.idJdbcType = idJdbcType;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public boolean isBlob() {
        return isBlob;
    }

    public void setBlob(boolean blob) {
        isBlob = blob;
    }

    public List<Field> getBlobFieldList() {
        return blobFieldList;
    }

    public void setBlobFieldList(List<Field> blobFieldList) {
        this.blobFieldList = blobFieldList;
    }
}

public class MapperImplGenerator {
    private static String oldPath = "E:\\javawork\\patest2\\src\\main\\resources\\mybatis_mappers";
    private static String newPath = oldPath + "/impl";

    public static void main(String[] args) throws IOException {
        File oldPathFile = new File(oldPath);
        File[] files = oldPathFile.listFiles();
        int count = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            Element rootElement = readXml(file.getPath());
            Model model = getModel(rootElement);
            System.out.println(JSON.toJSONString(model));
            generator(model, file.getName());
            if (count++ >= 100) {
                break;
            }
        }
    }

    private static void generator(Model model, String fileName) throws IOException {
        File newFile = new File(newPath + "/" + fileName);
        if (newFile.exists()) {
            return;
        } else {
            newFile.createNewFile();
        }
        PrintWriter printWriter = new PrintWriter(newFile);
        printWriter.printf("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");
        printWriter.printf("<mapper namespace=\"%s\">\n", model.getNamespace());
        printWriter.printf("    <select id=\"selectByCondition\" resultMap=\"BaseResultMap\" parameterType=\"java.util.Map\">\n" +
                "        SELECT\n");
        printWriter.printf("        <include refid=\"Base_Column_List\"/>\n");
        if (model.isBlob()) {
            printWriter.printf("        ,\n" +
                    "        <include refid=\"Blob_Column_List\"/>\n");
        }
        printWriter.printf("        FROM %s\n" +
                "        WHERE 1 = 1\n", model.getTableName());
        printfField(printWriter, model.getIdColumn(), model.getIdProperty());
        for (Field field : model.getFieldList()) {
            printfField(printWriter, field.getColumn(), field.getProperty());
        }
        for (Field field : model.getBlobFieldList()) {
            printfField(printWriter, field.getColumn(), field.getProperty());
        }
        printWriter.printf("        <if test=\"start != null and rows != null\">\n" +
                "            LIMIT #{start}, #{rows}\n" +
                "        </if>\n" +
                "    </select>\n");
        printWriter.printf("    <select id=\"selectByConditionGetCount\" resultType=\"java.lang.Integer\" parameterType=\"java.util.Map\">\n" +
                "        SELECT\n" +
                "        count(*)\n");
        printWriter.printf("        FROM %s\n" +
                "        WHERE 1 = 1\n", model.getTableName());
        printfField(printWriter, model.getIdColumn(), model.getIdProperty());
        for (Field field : model.getFieldList()) {
            printfField(printWriter, field.getColumn(), field.getProperty());
        }
        for (Field field : model.getBlobFieldList()) {
            printfField(printWriter, field.getColumn(), field.getProperty());
        }
        printWriter.printf("        <if test=\"start != null and rows != null\">\n" +
                "            LIMIT #{start}, #{rows}\n" +
                "        </if>\n" +
                "    </select>\n");
        printWriter.printf("    <delete id=\"deleteByIdsList\" parameterType=\"java.util.Map\">\n" +
                "        delete from %s\n" +
                "        where use_pro_id in\n" +
                "        <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">\n" +
                "            #{item}\n" +
                "        </foreach>\n" +
                "    </delete>\n", model.getTableName());
        printWriter.printf("</mapper>");
        printWriter.flush();
        printWriter.close();
    }

    public static void printfField(PrintWriter printWriter, String cc, String pp) {
        printWriter.printf("        <if test=\"record.%s != null\">\n" +
                "            AND\n" +
                "            %s = #{record.%s}\n" +
                "        </if>\n", pp, cc, pp);
    }


    public static Model getModel(Element rootElement) {
        Model model = new Model();
        model.setNamespace(rootElement.attributeValue("namespace"));
        List<Element> resultMapList = rootElement.elements("resultMap");
        Element firltMap = resultMapList.get(0);
        // 设置type
        model.setType(firltMap.attributeValue("type"));
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

    public static List<Field> getField(Element rootElement) {
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

    public static Element readXml(String filePath) {
        InputStream in = null;
        Element rootElement = null;
        try {
            SAXReader reader = new SAXReader();
            in = new FileInputStream(new File(filePath));
            Document doc = reader.read(in);
            rootElement = doc.getRootElement();
            // System.out.println("XMLUtil.readXml root name:" + rootElement.getName());
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
