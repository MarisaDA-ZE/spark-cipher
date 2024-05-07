package top.kirisamemarisa.sparkcipher.entity.enums;

/**
 * @Author Marisa
 * @Description 登录方式枚举
 * @Date 2023/11/30
 */
public enum JwtKeys {
    UID("uid"),
    ACCOUNT("account"),
    DEVICE_ID("deviceId"),
    TIME("time");
    private final String key;

    JwtKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
