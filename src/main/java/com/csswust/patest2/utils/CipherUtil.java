package com.csswust.patest2.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

/**
 * @author 972536780
 */
public class CipherUtil {
    /**
     * @param str 加密字符串
     * @return 加密后字符串
     */
    public static String encode(String str) {
        return getSHA256Str(str);
    }

    /***
     *  利用Apache的工具类实现SHA-256加密
     */
    private static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(encdeStr)) return str;
        else return encdeStr;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getSHA256Str("Yan230Kd"));
    }
}
