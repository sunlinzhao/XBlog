package com.slz.xblog.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    // 使用 MD5 加盐加密
    public static String md5WithSalt(String password, String email) {
        String salt = email; // 使用整个邮箱作为盐
        String saltedPassword = password + salt; // 组合密码和盐
        return md5(saltedPassword);
    }

    // MD5 加密
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
