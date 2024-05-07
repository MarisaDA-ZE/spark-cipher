package top.kirisamemarisa.sparkcipher.entity.enums;

/**
 * @Author Marisa
 * @Description 登录方式枚举
 * @Date 2023/11/30
 */
public enum LoginType {
    ACCOUNT(0),
    PHONE(1),
    EMAIL(2);
    private final Integer loginType;

    LoginType(Integer type) {
        this.loginType = type;
    }

    public Integer getLoginType() {
        return this.loginType;
    }
}
