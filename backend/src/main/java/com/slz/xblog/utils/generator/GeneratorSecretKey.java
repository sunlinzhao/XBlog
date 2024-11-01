package com.slz.xblog.utils.generator;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/22
 */

public class GeneratorSecretKey {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        secureRandom.nextBytes(key);
        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated SECRET_KEY: " + secretKey);
    }
}

