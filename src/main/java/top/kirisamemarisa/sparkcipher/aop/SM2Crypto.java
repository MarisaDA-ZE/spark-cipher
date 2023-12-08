package top.kirisamemarisa.sparkcipher.aop;
import top.kirisamemarisa.sparkcipher.aop.enums.CRYPTO_TYPE;

import java.lang.annotation.*;


/**
 * @Author Marisa
 * @Description Crypto.描述
 * @Date 2023/12/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SM2Crypto {
    CRYPTO_TYPE value() default CRYPTO_TYPE.DECRYPT; // 处理方式(加密或解密)
}
