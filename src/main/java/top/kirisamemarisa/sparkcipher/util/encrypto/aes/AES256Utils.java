package top.kirisamemarisa.sparkcipher.util.encrypto.aes;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * @Author Marisa
 * @Description AES256加密工具类
 * @Date 2023/11/28
 */

@Component
public class AES256Utils {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;

    private static final String SECRET_KEY = "CWOPE7iin6a9rMhI7tJus4Fh0uHBt6VEMkqsIYUlnMs=";
    private static final String IV_KEY = "OIPCSsv76Vl1FsQSc68XmA==";


    public static void main(String[] args) throws Exception {
        String plainText = "这是一个需要加密的字符串";
        SecretKey sk = generateSecretKey();
        byte[] iv = generateIV();
        String sk_s = Base64.getEncoder().encodeToString(sk.getEncoded());
        String iv_s = Base64.getEncoder().encodeToString(iv);
        System.out.println("sk: " + sk_s);
        System.out.println("iv: " + iv_s);

        String encryptedText = encrypt(plainText, sk, iv);
        System.out.println("加密后的文本： " + encryptedText);
        String decryptedText = decrypt(encryptedText, sk, iv);
        System.out.println("解密后的文本： " + decryptedText);

    }

    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static String encrypt(String plainText) {
        String result = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
            SecretKey secretKey = new SecretKeySpec(bytes, "AES");
            byte[] iv = Base64.getDecoder().decode(IV_KEY);
            result = encrypt(plainText, secretKey, iv);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encrypt(String plainText, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) {
        String result;
        try {
            byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
            SecretKey secretKey = new SecretKeySpec(bytes, "AES");
            byte[] iv = Base64.getDecoder().decode(IV_KEY);
            result = decrypt(encryptedText, secretKey, iv);
        } catch (Exception e) {
            e.printStackTrace();
            return encryptedText;
        }
        return result;
    }

    public static String decrypt(String encryptedText, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            // System.out.println("转换出错: " + ex.getMessage());
        }
        return encryptedText;
    }
}