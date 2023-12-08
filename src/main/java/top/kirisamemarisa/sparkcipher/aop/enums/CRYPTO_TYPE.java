package top.kirisamemarisa.sparkcipher.aop.enums;


/**
 * @Author Marisa
 * @Description CRYPTO_TYPE.描述
 * @Date 2023/12/8
 */
public enum CRYPTO_TYPE {
    ENCRYPT(0),  // 加密
    DECRYPT(1);  // 解密

    private final Integer type;

    CRYPTO_TYPE(int type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }
}
