package com.csswust.patest2.utils;

import java.io.*;
import java.nio.charset.Charset;

public class StreamUtil {

    /**
     * 从输出流获取数据
     */
    public static String output(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String s = null;
        while ((s = reader.readLine()) != null) {
            sb.append(s).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * 向输入流输入数据
     */
    public static void input(OutputStream os, String cmd) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(cmd);
        writer.close();
    }
}
