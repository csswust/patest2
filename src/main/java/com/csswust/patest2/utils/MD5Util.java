package com.csswust.patest2.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 972536780
 */
public class MD5Util {
    /**
     * @param str 加密字符串
     * @return 加密后字符串
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String encode(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String encStr = Base64.encodeBase64String(md5.digest(str.getBytes("UTF-8")));
        return encStr;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encode("1"));
    }
}
