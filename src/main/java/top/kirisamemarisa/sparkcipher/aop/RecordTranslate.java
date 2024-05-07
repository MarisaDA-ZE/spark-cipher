package top.kirisamemarisa.sparkcipher.aop;

import top.kirisamemarisa.sparkcipher.aop.enums.FIELD_TYPE;

import java.lang.annotation.*;


/**
 * @Author Marisa
 * @Description 用于标识需要翻译的Vo字段是单个还是列表
 * @Date 2024/05/07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface RecordTranslate {
    FIELD_TYPE value() default FIELD_TYPE.M_RECORD_ITEM; // 字段类型
}
