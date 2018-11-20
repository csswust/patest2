package com.csswust.patest2.utils;

import sun.misc.BASE64Decoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by Wzy
 * on 2018/6/14 15:41
 * 不短不长八字刚好
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

  public static void write(InputStream inputStream, OutputStream outputStream, boolean close) throws IOException {
    byte[] by = new byte[1024];
    int n;
    while ((n = inputStream.read(by)) != -1) {
      outputStream.write(by, 0, n);
    }
    if (close) {
      inputStream.close();
      outputStream.close();
    }
  }

  public static List<String> read(InputStream inputStream, String charsetName) {
    InputStreamReader isr = null;
    try {
      isr = new InputStreamReader(inputStream, charsetName);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
    BufferedReader reader = new BufferedReader(isr);
    String str;
    List<String> rs = new ArrayList<>();
    try {
      while ((str = reader.readLine()) != null) {
        rs.add(str);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        reader.close();
        isr.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return rs;
  }

  public static boolean download(String path, String fileName, String file) {
    File parent = new File(path);
    if (!parent.exists()) {
      parent.mkdirs();
    }
    File target = new File(parent + "/" + fileName);
    if (!target.exists()) {
      try {
        target.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }
    BASE64Decoder decoder = new BASE64Decoder();
    try {
      byte[] b = decoder.decodeBuffer(file);
      for (int i = 0; i < b.length; ++i) {
        if (b[i] < 0) {// 调整异常数据
          b[i] += 256;
        }
      }
      OutputStream out = new FileOutputStream(target);
      out.write(b);
      out.flush();
      out.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }


  public static void download(String fileName, InputStream inputStream, HttpServletResponse response) throws IOException {
    response.setContentType("application/x-msdownload");
    response.setHeader("Content-Disposition", "attachment;filename=" + "\"" + new Date().getTime() + fileName + "\"");
    ServletOutputStream outputStream = response.getOutputStream();
    write(inputStream, outputStream, true);
  }
}
