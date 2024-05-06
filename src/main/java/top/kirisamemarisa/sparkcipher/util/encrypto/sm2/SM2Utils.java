//package top.kirisamemarisa.sparkcipher.util.encrypto.sm2;
//
//import cn.hutool.core.util.HexUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.crypto.asymmetric.KeyType;
//import cn.hutool.crypto.asymmetric.SM2;
//import top.kirisamemarisa.sparkcipher.entity.SM2KeyPair;
//
//
//import java.math.BigInteger;
//import java.security.interfaces.ECPrivateKey;
//import java.security.interfaces.ECPublicKey;
//
//
///**
// * @author Marisa
// * @description SM2工具类
// * @date 2022/11/10
// */
//public class SM2Utils {
//
//    /**
//     * 生成SM2公私钥
//     *
//     * @return .
//     */
//    public static SM2KeyPair generateKeyPair() {
//        SM2 sm2 = new SM2();
//        ECPublicKey publicKey = (ECPublicKey) sm2.getPublicKey();
//        ECPrivateKey privateKey = (ECPrivateKey) sm2.getPrivateKey();
//        // 获取公钥
//        // byte[] publicKeyBytes = publicKey.getQ().getEncoded(false);
//        byte[] publicKeyBytes = publicKey.getEncoded();
//        String publicKeyHex = HexUtil.encodeHexStr(publicKeyBytes);
//
//        // 获取64位私钥
//        // String privateKeyHex = privateKey.getD().toString(16);
//        byte[] pkArr = privateKey.getEncoded();
//        String privateKeyHex = (new BigInteger(pkArr)).toString(16);
//        // BigInteger转成16进制时，不一定长度为64，如果私钥长度小于64，则在前方补0
//        StringBuilder privateKey64 = new StringBuilder(privateKeyHex);
//        while (privateKey64.length() < 64) {
//            privateKey64.insert(0, "0");
//        }
//        return new SM2KeyPair(publicKeyHex, privateKey64.toString());
//    }
//
//    /**
//     * SM2私钥签名
//     *
//     * @param privateKey 私钥
//     * @param content    待签名内容
//     * @return 签名值
//     */
//    public static String sign(String privateKey, String content) {
//        // SM2 sm2 = new SM2(privateKey, null);
//        // return sm2.signHex(HexUtil.encodeHexStr(content));
//        return null;
//    }
//
//    /**
//     * SM2公钥验签
//     *
//     * @param publicKey 公钥
//     * @param content   原始内容
//     * @param sign      签名
//     * @return 验签结果
//     */
//    public static boolean verify(String publicKey, String content, String sign) {
//        // SM2 sm2 = new SM2(null, publicKey);
//        // return sm2.verifyHex(HexUtil.encodeHexStr(content), sign);
//        return false;
//    }
//
//    /**
//     * SM2公钥加密
//     *
//     * @param content   原文
//     * @param publicKey SM2公钥
//     * @return .
//     */
//    public static String encrypt(String content, String publicKey) {
//        // SM2 sm2 = new SM2(null, publicKey);
//        // return sm2.encryptBase64(content, KeyType.PublicKey);
//        return null;
//    }
//
//    /**
//     * SM2私钥解密
//     *
//     * @param encryptStr SM2加密字符串
//     * @param privateKey SM2私钥
//     * @return .
//     */
//    public static String decrypt(String encryptStr, String privateKey) {
//        SM2 sm2 = new SM2(privateKey, null);
//        return StrUtil.utf8Str(sm2.decrypt(encryptStr, KeyType.PrivateKey));
//    }
//
//
//    public static void main(String[] args) {
//        SM2KeyPair sm2KeyPair = generateKeyPair();
//        String publicKey = sm2KeyPair.getPublicKey();
//        String privateKey = sm2KeyPair.getPrivateKey();
//        System.out.println("公钥: " + publicKey);
//        System.out.println("私钥: " + privateKey);
//        String text = "Marisa";
//        String encrypt = encrypt(text, publicKey);
//        System.out.println("原始文本: " + text);
//        System.out.println("加密结果: " + encrypt);
//        System.out.println("解密结果: " + decrypt(encrypt, privateKey));
//
//        String signed = sign(encrypt, privateKey);
//        System.out.println("签名: " + signed);
//    }
//}
//
