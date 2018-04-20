package com.csswust.patest2.test;

import com.csswust.patest2.utils.StreamUtil;

import java.io.IOException;

/**
 * Created by 972536780 on 2018/4/19.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("ping 222.196.35.228");
            String errMsg = StreamUtil.output(process.getErrorStream());
            // 获得控制台信息
            String consoleMsg = StreamUtil.output(process.getInputStream());
            System.out.println(consoleMsg);
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
