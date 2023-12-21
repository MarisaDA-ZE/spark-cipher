package top.kirisamemarisa.sparkcipher.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.common.enums.TypeSuffix;
import top.kirisamemarisa.sparkcipher.entity.NegotiateKeyPair;
import top.kirisamemarisa.sparkcipher.entity.SM2KeyPair;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.ICryptoService;
import top.kirisamemarisa.sparkcipher.util.encrypto.sm2.SM2Utils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static top.kirisamemarisa.sparkcipher.common.Constants.KEYPAIR_EXPIRE_TIME;

/**
 * @Author Marisa
 * @Description CryptoServiceImpl.描述
 * @Date 2023/12/8
 */
@Service
public class CryptoServiceImpl implements ICryptoService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String negotiateKeyPair(NegotiateKeyPair negotiateKeyPair) {
        if (ObjectUtils.isEmpty(negotiateKeyPair) || StringUtils.isBlank(negotiateKeyPair.getPublicKey()))
            throw new UnauthorizedException("密钥信息异常！");
        String finger = negotiateKeyPair.getF() + "";
        SM2KeyPair ckp = new SM2KeyPair(negotiateKeyPair.getPublicKey(), null);
        redisTemplate.opsForValue().set(finger + TypeSuffix.CKP.getTypeSuffix(), ckp, KEYPAIR_EXPIRE_TIME, TimeUnit.SECONDS);
        // 生成服务端密钥对并保存到redis
        SM2KeyPair skp = SM2Utils.generateKeyPair();
        redisTemplate.opsForValue().set(finger + TypeSuffix.SKP.getTypeSuffix(), skp, KEYPAIR_EXPIRE_TIME, TimeUnit.SECONDS);
        System.out.println("服务端密钥: " + skp);
        System.out.println("客户端密钥: " + ckp);
        return skp.getPublicKey();
    }
}
