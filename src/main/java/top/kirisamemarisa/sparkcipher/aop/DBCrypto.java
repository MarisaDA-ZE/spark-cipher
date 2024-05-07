package top.kirisamemarisa.sparkcipher.aop;

import java.lang.annotation.*;


/**
 * @Author Marisa
 * @Description 用于标识是否在数据库中还有加密
 * @Date 2024/05/07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DBCrypto {
}
