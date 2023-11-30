package top.kirisamemarisa.sparkcipher.util.sm2;

import top.kirisamemarisa.sparkcipher.util.sm2.SM2KeyPair;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * @Author Marisa
 * @Description SM2工具类
 * @Date 2023/11/29
 */
public class SM2Utils {

    /**
     * 生成随机密钥对
     *
     * @return 随机密钥对
     */
    public static SM2KeyPair generateKeyPair() {
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key;
        do {
            key = sm2.ecc_key_pair_generator.generateKeyPair();
        } while (((ECPrivateKeyParameters) key.getPrivate()).getD().toByteArray().length != 32);
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        // publicKey.getEncoded()参数补充为false     Marisa 2022-11-10
        String pub_k = UtilForSM2.byteToHex(publicKey.getEncoded(false));
        String pri_k = UtilForSM2.byteToHex(privateKey.toByteArray());
        return new SM2KeyPair(pub_k, pri_k);
    }

    /**
     * 数据加密
     *
     * @param publicKey 公钥
     * @param data      数据
     * @return 加密结果
     */
    public static String encrypt(byte[] publicKey, byte[] data) {
        if (publicKey == null || publicKey.length == 0) {
            return null;
        }
        if (data == null || data.length == 0) {
            return null;
        }
        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);
        Cipher cipher = new Cipher();
        SM2 sm2 = SM2.Instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);
        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);
        // c1.getEncoded()参数补充为false    Marisa 2022-11-10
        return UtilForSM2.byteToHex(c1.getEncoded(false)) + UtilForSM2.byteToHex(c3) + UtilForSM2.byteToHex(source);
    }

    /**
     * 数据加密
     *
     * @param text      明文字符串
     * @param publicKey 公钥
     * @return 加密结果
     */
    public static String encrypt(String text, String publicKey) {
        if (StringUtils.isBlank(text)) return null;
        if (StringUtils.isBlank(publicKey)) return null;
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] key = UtilForSM2.hexToByte(publicKey);
        return encrypt(key, bytes);
    }

    /**
     * 数据解密
     *
     * @param text       待解密文本
     * @param privateKey 私钥
     * @return 解密结果
     */
    public static String decrypt(String text, String privateKey) {
        if (StringUtils.isBlank(text)) return null;
        if (StringUtils.isBlank(privateKey)) return null;
        byte[] data = UtilForSM2.hexToByte(text);
        byte[] privateK = UtilForSM2.hexToByte(privateKey);
        // 加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        byte[] c1Bytes = UtilForSM2.hexToByte(text.substring(0, 130));
        assert data != null;
        int c2Len = data.length - 97;
        byte[] c3 = UtilForSM2.hexToByte(text.substring(130, 130 + 64));
        byte[] c2 = UtilForSM2.hexToByte(text.substring(194, 194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        assert privateK != null;
        BigInteger userD = new BigInteger(1, privateK);
        // 通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        assert c2 != null;
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);
        // 返回解密结果
        return new String(c2);
    }

    public static void main(String[] args) {
        SM2KeyPair sm2KeyPair = generateKeyPair();
        String publicKey = sm2KeyPair.getPublicKey();
        String privateKey = sm2KeyPair.getPrivateKey();
        System.out.println("公钥: " + publicKey);
        System.out.println("私钥: " + privateKey);
        String text = "Marisa";
        String encrypt = encrypt(text, publicKey);
        System.out.println("原始文本: " + text);
        System.out.println("加密结果: " + encrypt);
        System.out.println("解密结果: " + decrypt(encrypt, privateKey));
    }
}

