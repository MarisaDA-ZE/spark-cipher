package top.kirisamemarisa.sparkcipher.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kirisamemarisa.sparkcipher.entity.SM2KeyPair;
import top.kirisamemarisa.sparkcipher.entity.User;
import top.kirisamemarisa.sparkcipher.exception.UnauthorizedException;
import top.kirisamemarisa.sparkcipher.service.ICryptoService;
import top.kirisamemarisa.sparkcipher.util.SecurityUtils;

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
    private SecurityUtils securityUtils;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String getServicePublicKey() {
        User authUser = securityUtils.getAuthUser();
        Object o = redisTemplate.opsForValue().get(authUser.getId() + ".skp");
        if (ObjectUtils.isEmpty(o)) throw new UnauthorizedException("服务端密钥过期！");
        SM2KeyPair keyPair = (SM2KeyPair) o;
        return keyPair.getPublicKey();
    }

    @Override
    public void setClientPublicKey(SM2KeyPair clientKeyPair) {
        if (ObjectUtils.isEmpty(clientKeyPair) || StringUtils.isBlank(clientKeyPair.getPublicKey()))
            throw new UnauthorizedException("密钥信息异常！");
        SM2KeyPair keyPair = new SM2KeyPair(clientKeyPair.getPublicKey(), null);
        System.out.println("客户端公钥: " + keyPair);
        User authUser = securityUtils.getAuthUser();
        String key = authUser.getId() + ".ckp";
        redisTemplate.opsForValue().set(key, keyPair, KEYPAIR_EXPIRE_TIME, TimeUnit.SECONDS);
    }
}
