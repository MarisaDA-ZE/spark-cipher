package top.kirisamemarisa.sparkcipher.aop.impl;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.kirisamemarisa.sparkcipher.aop.SM2Crypto;
import top.kirisamemarisa.sparkcipher.aop.enums.CRYPTO_TYPE;
import top.kirisamemarisa.sparkcipher.entity.SM2KeyPair;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;
import top.kirisamemarisa.sparkcipher.util.encrypto.sm2.SM2Utils;

import javax.annotation.Resource;
import java.lang.reflect.Method;


/**
 * @author Marisa
 * @description DecryptAspect.描述
 * @date 2022/11/2
 */
@Aspect
@Component
public class CryptoAspect {
    @Value("${mrs.enable-encrypt-link}")
    private Boolean enableEncrypt;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 加密解密数据
     *
     * @param joinPoint jp
     * @return returning .
     * @throws Throwable .
     */
    @Around("@annotation(cryptoData)")
    public Object cryptoData(ProceedingJoinPoint joinPoint, SM2Crypto cryptoData) throws Throwable {
        // 根据配置文件决定是否加载加密模块
        if (!enableEncrypt) return joinPoint.proceed(joinPoint.getArgs());
        CRYPTO_TYPE value = cryptoData.value();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SM2Crypto annotation = method.getAnnotation(SM2Crypto.class);
        CRYPTO_TYPE value1 = annotation.value();
        System.out.println("crypto type: " + value + ", " + value1);
        Object[] args = joinPoint.getArgs();
        // 解密
        if (value == CRYPTO_TYPE.DECRYPT && !ObjectUtils.isEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                String str = args[i].toString();
                String regExp = "[~`!@#$%^&*()_+\\-=<>?,./;:'\"\\[\\]{}|\\\\]";
                String text = str.replaceAll(regExp, "");
                args[i] = decrypt(text);
            }
        }
        Object result = joinPoint.proceed(args);
        // 加密
        if (value == CRYPTO_TYPE.ENCRYPT && !ObjectUtils.isEmpty(result)) {
            String text = result.toString();
            result = encrypt(text);
        }
        return result;
    }

    /**
     * 加密
     *
     * @param text 待加密文本
     * @return 加密后的密文
     */
    private String encrypt(String text) {
        if (StringUtils.isBlank(text)) return null;
        User authUser = securityUtils.getAuthUser();
        Object o = redisTemplate.opsForValue().get(authUser.getId() + ".ckp");
        if (ObjectUtils.isEmpty(o)) throw new UnauthorizedException("客户端密钥对已过期！");
        SM2KeyPair keyPair = (SM2KeyPair) o;
        String key = keyPair.getPublicKey();
        try {
            return SM2Utils.encrypt(text, key);
        } catch (Exception e) {
            // throw new EncryptException(ENCRYPT_ERROR);
            return text;
        }
    }

    /**
     * 解密
     *
     * @param text 待解密的密文
     * @return 解密出的明文
     */
    private String decrypt(String text) {
        if (StringUtils.isBlank(text)) return null;
        User authUser = securityUtils.getAuthUser();
        Object o = redisTemplate.opsForValue().get(authUser.getId() + ".skp");
        if (ObjectUtils.isEmpty(o)) throw new UnauthorizedException("服务端密钥对已过期！");
        SM2KeyPair keyPair = (SM2KeyPair) o;
        String key = keyPair.getPublicKey();
        try {
            return SM2Utils.decrypt(text, key);
        } catch (Exception e) {
            // throw new DecryptException(DECRYPT_ERROR);
            return text;
        }
    }
}
