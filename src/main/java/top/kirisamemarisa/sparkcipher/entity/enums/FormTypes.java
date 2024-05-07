package top.kirisamemarisa.sparkcipher.entity.enums;

/**
 * @Author Marisa
 * @Description 表单类型枚举
 * @Date 2024/5/7
 */
public enum FormTypes {
    TEXT("text"),
    TEXTAREA("textarea"),
    PASSWORD("password"),
    BUTTON("button"),
    CHECKBOX("checkbox"),
    FILE("file"),
    NUMBER("number"),
    RADIO("radio");
    private final String name;

    FormTypes(String name) {
        this.name = name;
    }

    public String getType() {
        return this.name;
    }
}
