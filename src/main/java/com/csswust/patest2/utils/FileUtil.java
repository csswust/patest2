package com.csswust.patest2.utils;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FileUtil {
    /**
     * @param bytes
     * @param filePath
     * @param fileName
     * @since 2016/1/3
     */
    public static void getFileFromBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 将file转化为比特数组
     *
     * @param file
     * @return
     * @since 2016/1/3
     */
    public static byte[] getBytesFromFile(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 生成文件
     *
     * @param src
     * @param path
     * @param fileName
     * @since 2016/1/3
     */
    public static void generateFile(String src, String path, String fileName) {
        try {
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(path + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                while (true) {
                    file.delete();
                    if (!file.exists()) {
                        break;
                    }
                }
                file.createNewFile();
            }
            BufferedOutputStream bs = new BufferedOutputStream(
                    new FileOutputStream(file));
            bs.write(src.getBytes());
            bs.flush();
            bs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件
     *
     * @param path
     * @param fileName
     */
    public static void removeFile(String path, String fileName) {
        File file = new File(path + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 读取文件
     *
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     * @author wwhhff11
     * @since 2016/1/3
     */
    public static String readFile(String path, String fileName)
            throws IOException {
        String filePath = path + File.separator + fileName;
        BufferedReader br = new BufferedReader(
                new FileReader(new File(filePath)));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

    /**
     * 整理目录，使文件个数保持在一个范围
     *
     * @param dir_path
     * @param number
     * @since 2016/03/02
     */
    public static void cleanUpFileNumber(String dir_path, Integer number) {
        File dir = new File(dir_path);
        if (dir.isDirectory()) {
            List<File> files = Arrays.asList(dir.listFiles());
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o2.getName().compareTo(o1.getName());
                }
            });
            if (files.size() >= number) {
                for (int i = number - 1; i < files.size(); i++) {
                    files.get(i).delete();
                }
            }
        }
    }
}
