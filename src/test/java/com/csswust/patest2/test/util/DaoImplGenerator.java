package com.csswust.patest2.test.util;

import java.io.*;
import java.util.Scanner;

/**
 * Created by 972536780 on 2017/11/20.
 */
public class DaoImplGenerator {
    private static String oldPath = "E:\\javawork\\patest2\\src\\main\\java\\com\\csswust\\patest2\\dao";
    private static String newPath = oldPath + "/impl";

    public static void main(String[] args) throws IOException {
        File oldPathFile = new File(oldPath);
        File[] files = oldPathFile.listFiles();
        for (File file : files) {
            if (file.isDirectory() || file.getName().contains("Common")) {
                continue;
            }
            Scanner in = new Scanner(new FileInputStream(file));
            String packageLine = in.nextLine();
            packageLine = packageLine.substring(8, packageLine.length() - 1);
            in.nextLine();
            String importLine = in.nextLine();
            in.nextLine();
            in.next();
            in.next();
            String calssName = in.next();
            calssName = calssName.substring(0, calssName.length() - 3);
            in.next();
            in.next();
            in.next();
            String primaryName = in.next();
            primaryName = primaryName.substring(0, primaryName.length() - 2);
            String daPrimaryName = primaryName.substring(0, 1).toUpperCase() + primaryName.substring(1);
            boolean flag = false;
            while (in.hasNext()) {
                String temp = in.nextLine();
                if (temp.contains("BLOBs")) {
                    flag = true;
                    break;
                }
            }
            generator(packageLine, importLine, calssName, primaryName, daPrimaryName, file.getName(), flag);
            in.close();
        }
    }

    private static void generator(String packageLine, String importLine, String calssName,
                                  String primaryName, String daPrimaryName, String newFileName,
                                  boolean flag) throws IOException {
        File newFile = new File(newPath + "/" + calssName + "DaoImpl.java");
        if (newFile.exists()) {
            return;
        } else {
            newFile.createNewFile();
        }
        PrintWriter printWriter = new PrintWriter(newFile);
        printWriter.printf("package %s.impl;\n\n", packageLine);
        printWriter.printf("%s\n", importLine);
        printWriter.printf("import %s.CommonMapper;\n", packageLine);
        printWriter.printf("import %s.%sDao;\n", packageLine, calssName);
        printWriter.printf("import org.springframework.stereotype.Repository;\n\n");
        printWriter.printf("import java.util.Date;\n\n@Repository\n");
        printWriter.printf("public class %sDaoImpl extends CommonMapper implements %sDao {\n", calssName, calssName);
        printWriter.printf("    private final static String PACKAGE = \"%s.%sDao.\";\n\n", packageLine, calssName);
        printWriter.printf("    @Override\n" +
                "    public int deleteByPrimaryKey(Integer %s) {\n" +
                "        if (%s == null) {\n" +
                "            return 0;\n" +
                "        }\n" +
                "        return getSqlSession().delete(PACKAGE + \"deleteByPrimaryKey\", %s);\n" +
                "    }\n\n", primaryName, primaryName, primaryName);
        printWriter.printf("    @Override\n" +
                "    public int insert(%s record) {\n" +
                "        if (record == null) {\n" +
                "            return 0;\n" +
                "        }\n" +
                "        record.set%s(null);\n" +
                "        record.setCreateTime(new Date());\n" +
                "        return getSqlSession().insert(PACKAGE + \"insert\", record);\n" +
                "    }\n\n", calssName, daPrimaryName);
        printWriter.printf("    @Override\n" +
                "    public int insertSelective(%s record) {\n" +
                "        if (record == null) {\n" +
                "            return 0;\n" +
                "        }\n" +
                "        record.set%s(null);\n" +
                "        record.setCreateTime(new Date());\n" +
                "        return getSqlSession().insert(PACKAGE + \"insertSelective\", record);\n" +
                "    }\n\n", calssName, daPrimaryName);
        printWriter.printf("    @Override\n" +
                "    public %s selectByPrimaryKey(Integer %s) {\n" +
                "        if (%s == null) {\n" +
                "            return null;\n" +
                "        }\n" +
                "        return getSqlSession().selectOne(PACKAGE + \"selectByPrimaryKey\", %s);\n" +
                "    }\n\n", calssName, primaryName, primaryName, primaryName);
        printWriter.printf("    @Override\n" +
                "    public int updateByPrimaryKeySelective(%s record) {\n" +
                "        if (record == null || record.get%s() == null) {\n" +
                "            return 0;\n" +
                "        }\n" +
                "        record.setModifyTime(new Date());\n" +
                "        return getSqlSession().update(PACKAGE + \"updateByPrimaryKeySelective\", record);\n" +
                "    }\n\n", calssName, daPrimaryName);
        if (flag) {
            printWriter.printf("    @Override\n" +
                    "    public int updateByPrimaryKeyWithBLOBs(%s record) {\n" +
                    "        if (record == null || record.get%s() == null) {\n" +
                    "            return 0;\n" +
                    "        }\n" +
                    "        record.setModifyTime(new Date());\n" +
                    "        return getSqlSession().update(PACKAGE + \"updateByPrimaryKeyWithBLOBs\", record);\n" +
                    "    }\n\n", calssName, daPrimaryName);
        }
        printWriter.printf("    @Override\n" +
                "    public int updateByPrimaryKey(%s record) {\n" +
                "        if (record == null || record.get%s() == null) {\n" +
                "            return 0;\n" +
                "        }\n" +
                "        record.setModifyTime(new Date());\n" +
                "        return getSqlSession().update(PACKAGE + \"updateByPrimaryKey\", record);\n" +
                "    }\n" +
                "}\n", calssName, daPrimaryName);
        printWriter.flush();
        printWriter.close();
    }
}
