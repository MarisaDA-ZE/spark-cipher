package top.kirisamemarisa.sparkcipher.controller;


import org.springframework.web.bind.annotation.*;
import top.kirisamemarisa.sparkcipher.common.MrsResult;
import top.kirisamemarisa.sparkcipher.entity.SM2KeyPair;
import top.kirisamemarisa.sparkcipher.service.ICryptoService;

import javax.annotation.Resource;

/**
 * @Author Marisa
 * @Description CryptoController.描述
 * @Date 2023/12/8
 */
@CrossOrigin
@RestController
@RequestMapping("/crypto")
public class CryptoController {

    @Resource
    private ICryptoService cryptoService;

    /**
     * 获取服务端响应请求的公钥
     *
     * @return 服务端响应请求的公钥
     */
    @GetMapping("/getServicePublicKey")
    public MrsResult<?> getServicePublicKey() {
        String key = cryptoService.getServicePublicKey();
        return MrsResult.ok("公钥获取成功", key);
    }

    /**
     * 设置客户端加密数据的公钥
     *
     * @param keyPair 客户端的公钥
     * @return 结果
     */
    @PostMapping("/setClientPublicKey")
    public MrsResult<?> setClientPublicKey(@RequestBody SM2KeyPair keyPair) {
        System.out.println("客户端公钥: " + keyPair);
        cryptoService.setClientPublicKey(keyPair);
        return MrsResult.ok("公钥更新成功");
    }
}
