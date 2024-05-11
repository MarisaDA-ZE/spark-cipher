package top.kirisamemarisa.sparkcipher.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Marisa
 * @Description 用于标识一个字段是否允许在数据库中重复
 * <p>目前只是一个软性规定，重复了也不会出错</p>
 * @Date 2024/05/11
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueField {
    /**
     * 是否忽略空值（NULL）进行唯一性校验
     */
    boolean ignoreNull() default true;

}
