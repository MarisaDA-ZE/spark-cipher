package top.kirisamemarisa.sparkcipher.util;

import java.security.SecureRandom;

/**
 * @Author Marisa
 * @Description 随机盐工具类
 * @Date 2023/11/28
 */
public class SaltGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            salt.append(CHARACTERS.charAt(index));
        }
        return salt.toString();
    }

    /**
     * 生成随机盐
     *
     * @return .
     */
    public static String generateSalt() {
        return generateSalt(6);
    }

    public static void main(String[] args) {
        String salt = generateSalt();
        System.out.println("生成的盐为： " + salt);
    }
}