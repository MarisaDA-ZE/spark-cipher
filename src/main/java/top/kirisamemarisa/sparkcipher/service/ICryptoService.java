package top.kirisamemarisa.sparkcipher.service;

import top.kirisamemarisa.sparkcipher.entity.SM2KeyPair;

/**
 * @Author Marisa
 * @Description 加密服务
 * @Date 2023/12/8
 */
public interface ICryptoService {

    /**
     * 获取服务端公开的公钥
     *
     * @return 公钥
     */
    String getServicePublicKey();

    /**
     * 设置客户端加密用的公钥
     *
     * @param clientKeyPair 客户端公钥
     */
    void setClientPublicKey(SM2KeyPair clientKeyPair);
}
