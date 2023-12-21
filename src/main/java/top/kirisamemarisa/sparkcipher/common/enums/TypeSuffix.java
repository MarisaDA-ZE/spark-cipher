package top.kirisamemarisa.sparkcipher.common.enums;

/**
 * @Author Marisa
 * @Description TypeSuffix.描述
 * @Date 2023/12/21
 */
public enum TypeSuffix {
    SKP(".skp"),
    CKP(".ckp");
    private final String suffix;

    TypeSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTypeSuffix() {
        return this.suffix;
    }
}
