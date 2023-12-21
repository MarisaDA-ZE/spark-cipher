package top.kirisamemarisa.sparkcipher.service;

import top.kirisamemarisa.sparkcipher.entity.NegotiateKeyPair;

/**
 * @Author Marisa
 * @Description 加密服务
 * @Date 2023/12/8
 */
public interface ICryptoService {

    /**
     * 协商密钥对
     *
     * @param keyPair 客户端公钥
     * @return 服务端公钥
     */
    String negotiateKeyPair(NegotiateKeyPair keyPair);
}
