package top.kirisamemarisa.sparkcipher.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author Marisa
 * @Description MD5工具类
 * @Date 2023/11/28
 */
public class MD5Util {

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法找到MD5算法", e);
        }
    }

    public static void main(String[] args) {
        String input = "测试字符串";
        String md5 = md5(input);
        System.out.println("MD5加密后的字符串： " + md5);
    }
}