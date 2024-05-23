package top.kirisamemarisa.sparkcipher.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Marisa
 * @Description 用于标识一个字段从指定配置文件中取得数据的键，格式为${<资源路径>}
 * @Date 2024/05/23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StaticValue {
    /**
     * 配置文件中的键值表达式
     */
    String value();
}